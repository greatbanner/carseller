package com.ca.agency.car.seller.service;

import java.time.LocalDateTime;

import com.ca.agency.car.seller.domain.Listing;
import com.ca.agency.car.seller.domain.ListingState;
import com.ca.agency.car.seller.domain.Question;
import com.ca.agency.car.seller.dto.AnswerDTO;
import com.ca.agency.car.seller.dto.QuestionDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.repository.IListingDAO;
import com.ca.agency.car.seller.repository.IQuestionDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    
    private final IQuestionDAO questionDao;
    private final IListingDAO listingDao;

    @Autowired
    public QuestionService(IQuestionDAO questionDao, IListingDAO listingDao){
        this.questionDao = questionDao;
        this.listingDao = listingDao;
    }

    public Question askDealer(QuestionDTO questionRequest) throws ServiceException{
        var listing = findListing(questionRequest.getListing());
        if(ListingState.DRAFT.equals(listing.getState())){
            throw new ServiceException(HttpStatus.CONFLICT,"This Listing is not published");
        }
        var listingQuestion = new Question(questionRequest.getFrom(), questionRequest.getMessage(), listing, LocalDateTime.now());
        return questionDao.save(listingQuestion);
    }

    public Question answerQuestion(AnswerDTO answerRequest) throws ServiceException{
        var question = findQuestion(answerRequest.getQuestion());
        if(question.getResponseDate() != null){
            throw new ServiceException(HttpStatus.CONFLICT,"This question is already answered");
        }
        question.setResponse(answerRequest.getResponse());
        question.setResponseDate(LocalDateTime.now());
        return questionDao.save(question);
    }

    private Listing  findListing(long listingID) throws ServiceException {
        return listingDao.findById(listingID).orElseThrow(() -> new ServiceException( HttpStatus.BAD_REQUEST,
                    new  StringBuilder("Listing with ID ")
                    .append(listingID+"")
                    .append(" not found. Please verify").toString()));
    }

    private Question findQuestion(long questionID) throws ServiceException{
        return questionDao.findById(questionID).orElseThrow((() -> new ServiceException( HttpStatus.BAD_REQUEST,
                    new  StringBuilder("Question with ID ")
                    .append(questionID+"")
                    .append(" not found. Please verify").toString())));
    }
}
