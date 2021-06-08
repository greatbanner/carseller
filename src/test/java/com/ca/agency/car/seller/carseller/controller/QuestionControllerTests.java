package com.ca.agency.car.seller.carseller.controller;

import static org.mockito.ArgumentMatchers.any;

import com.ca.agency.car.seller.controller.QuestionController;
import com.ca.agency.car.seller.domain.Question;
import com.ca.agency.car.seller.dto.AnswerDTO;
import com.ca.agency.car.seller.dto.QuestionDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.service.QuestionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.ca.agency.car.seller.carseller.controller.JsonUtil.asJsonString;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTests {
    @Mock
    private QuestionService serviceMock;
    
    @InjectMocks
    private QuestionController questionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(questionController)
                .build();
    }

    @Test
    @DisplayName("It should Created Question when do /api/listing/question POST with valid payload")
    public void shouldCreateQuestion() throws Exception{
        var createListingRequest = new QuestionDTO(1, "From", "Message");

        var questionResponse = new Question();
        long validListingId = 1;
        questionResponse.setId(validListingId);
        Mockito.when(serviceMock.askDealer(any())).thenReturn(questionResponse);

        mockMvc.perform( MockMvcRequestBuilders
            .post("/api/listing/question")
            .content(asJsonString(createListingRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    
    @Test
    @DisplayName("It should fail to create Question when serviceException is thrown")
    public void shouldFailCreateQuestion() throws Exception{
        var createListingRequest = new QuestionDTO(1, "From", "Message");

        var questionResponse = new Question();
        long validListingId = 1;
        questionResponse.setId(validListingId);
        Mockito.when(serviceMock.askDealer(any())).thenThrow(new ServiceException(HttpStatus.CONFLICT,"Error message"));

        mockMvc.perform( MockMvcRequestBuilders
            .post("/api/listing/question")
            .content(asJsonString(createListingRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("It should Answer Question when do /api/listing/question PUT with valid payload")
    public void shouldAnswerQuestion() throws Exception{
        var validAnswerRequest = new AnswerDTO(1, "Response");

        var questionResponse = new Question();
        long validListingId = 1;
        questionResponse.setId(validListingId);
        Mockito.when(serviceMock.answerQuestion(any())).thenReturn(questionResponse);

        mockMvc.perform( MockMvcRequestBuilders
            .put("/api/listing/question")
            .content(asJsonString(validAnswerRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
    
    @Test
    @DisplayName("It should fail to Answer Question when ServiceExeption is thrown")
    public void shouldFailAnswerQuestionWhenServiceExceptionThrown() throws Exception{
        var validAnswerRequest = new AnswerDTO(1, "Response");

        Mockito.when(serviceMock.answerQuestion(any())).thenThrow(new ServiceException(HttpStatus.CONFLICT, "Error message"));

        mockMvc.perform( MockMvcRequestBuilders
            .put("/api/listing/question")
            .content(asJsonString(validAnswerRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }
}
