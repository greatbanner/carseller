package com.ca.agency.car.seller.carseller.service;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.Optional;

import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.domain.Listing;
import com.ca.agency.car.seller.domain.ListingState;
import com.ca.agency.car.seller.domain.Question;
import com.ca.agency.car.seller.dto.AnswerDTO;
import com.ca.agency.car.seller.dto.QuestionDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.repository.IListingDAO;
import com.ca.agency.car.seller.repository.IQuestionDAO;
import com.ca.agency.car.seller.service.QuestionService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTests {
 
    @Mock
    private IQuestionDAO questionDAOMock;
 
    @Mock
    private IListingDAO listingDAOMock;

    @InjectMocks
    private QuestionService questionService;

    @Test
    @DisplayName("Should create a Question when askQuestion is requested with valida data")
    public void shouldCreateQuestionWithValidData() throws ServiceException{
        //Given:
        long validListingId = 1;
        var validQuestionRequest = new QuestionDTO(validListingId, "Valid From", "Valid message");
        var validListing = getValidListing(validListingId, new Dealer());
        //When:
        Mockito.when(listingDAOMock.findById(validListingId)).thenReturn(Optional.of(validListing));
        Mockito.when(questionDAOMock.save(any())).thenReturn(new Question());
        var response = questionService.askDealer(validQuestionRequest);
        //Then:
        Assertions.assertThatNoException();
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Should fail to create a Question when Listing not exists")
    public void shouldFailCreateQuestionWhenListingNotExists() {
        //Given:
        long invalidListingId = 1;
        var validQuestionRequest = new QuestionDTO(invalidListingId, "Valid From", "Valid message");
        //When:
        Mockito.when(listingDAOMock.findById(invalidListingId)).thenReturn(Optional.empty());
        try {
            questionService.askDealer(validQuestionRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(e.getMessage()).isEqualTo(
                new  StringBuilder("Listing with ID ")
                    .append(invalidListingId+"")
                    .append(" not found. Please verify").toString());
        }
    }

    @Test
    @DisplayName("Should fail to create a Question when Listing is in Draft state")
    public void shouldFailCreateQuestionWhenListingInDraft() {
        //Given:
        long validListingId = 1;
        var validQuestionRequest = new QuestionDTO(validListingId, "Valid From", "Valid message");
        var invalidListing = getValidListing(validListingId, new Dealer());
        invalidListing.setState(ListingState.DRAFT);
        //When:
        Mockito.when(listingDAOMock.findById(validListingId)).thenReturn(Optional.of(invalidListing));
        try {
            questionService.askDealer(validQuestionRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            Assertions.assertThat(e.getMessage()).isEqualTo("This Listing is not published");
        }
    }

    @Test
    @DisplayName("Should answer Question with valid Data")
    public void shouldAnsweQuestion() throws ServiceException{
        //Given:
        long validQuestionId = 1;
        var validAnswerRequest = new AnswerDTO(validQuestionId, "Valid response");
        var validQuestion = getValidQuestion(new Listing());
        //When:
        Mockito.when(questionDAOMock.findById(validQuestionId)).thenReturn(Optional.of(validQuestion));
        Mockito.when(questionDAOMock.save(any())).thenReturn(validQuestion);
        var response = questionService.answerQuestion(validAnswerRequest);
        //Then:
        Assertions.assertThatNoException();
        Assertions.assertThat(response.getResponseDate()).isNotNull();
    }
    
    @Test
    @DisplayName("Should fail to answer Question when Question not found")
    public void shouldFailAnsweQuestionWhenQuestionNotFound() {
        //Given:
        long invalidQuestionId = 1;
        var validAnswerRequest = new AnswerDTO(invalidQuestionId, "Valid response");
        //When:
        Mockito.when(questionDAOMock.findById(invalidQuestionId)).thenReturn(Optional.empty());
        try {
            questionService.answerQuestion(validAnswerRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(e.getMessage()).isEqualTo(new  StringBuilder("Question with ID ")
                        .append(invalidQuestionId+"")
                        .append(" not found. Please verify").toString());
        }
    }
      
    @Test
    @DisplayName("Should fail to answer Question when Question is already answered")
    public void shouldFailAnsweQuestionWhenQuestionAlreadyAnswered() {
        //Given:
        long invalidQuestionId = 1;
        var validAnswerRequest = new AnswerDTO(invalidQuestionId, "Valid response");
        var validQuestion = getValidQuestion(new Listing());
        validQuestion.setResponseDate(LocalDateTime.now());
        //When:
        Mockito.when(questionDAOMock.findById(invalidQuestionId)).thenReturn(Optional.of(validQuestion));
        try {
            questionService.answerQuestion(validAnswerRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            Assertions.assertThat(e.getMessage()).isEqualTo("This question is already answered");
        }
    }

    private Listing getValidListing(long id, Dealer dealer){
        var listing = new Listing(dealer, 3500.0, ListingState.PUBLISHED, "validBrand", "validModel");
        listing.setId(id);
        return listing;
    }

    private Question getValidQuestion(Listing listing){
        return new Question("validFrom", "message",listing , LocalDateTime.now());
    }
}
