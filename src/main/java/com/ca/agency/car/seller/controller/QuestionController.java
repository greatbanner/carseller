package com.ca.agency.car.seller.controller;

import com.ca.agency.car.seller.domain.Question;
import com.ca.agency.car.seller.dto.AnswerDTO;
import com.ca.agency.car.seller.dto.ExceptionResponseDTO;
import com.ca.agency.car.seller.dto.QuestionDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.service.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController()
@RequestMapping("/api/listing/question")
@Api(value = "Question", tags = { "Question" })
public class QuestionController {
    
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @PostMapping( consumes = "application/json", produces = "application/json")
    @ApiOperation("Helps you to ask a question about a Listing")
    @ApiResponses({
        @ApiResponse( code = 201, message = "Created", response = Question.class ),
        @ApiResponse( code = 404, message = "Listing not found", response = ExceptionResponseDTO.class )
    })
    public ResponseEntity<Question> askDealer(@RequestBody QuestionDTO questionResquest) {
        Question question;
        try {
            question = questionService.askDealer(questionResquest);
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                e.getHttpStatusCode(), e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    @PutMapping( consumes = "application/json", produces = "application/json")
    @ApiOperation("Helps you to answer a question about a Listing")
    @ApiResponses({
        @ApiResponse( code = 200, message = "Ok", response = Question.class ),
        @ApiResponse( code = 404, message = "Question not found", response = ExceptionResponseDTO.class )
    })
    public ResponseEntity<Question> answerQuestion(@RequestBody AnswerDTO answerResquest) {
        Question question;
        try {
            question = questionService.answerQuestion(answerResquest);
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                e.getHttpStatusCode(), e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }
}
