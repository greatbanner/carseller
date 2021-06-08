package com.ca.agency.car.seller.carseller.controller;

import static org.mockito.ArgumentMatchers.any;

import com.ca.agency.car.seller.controller.DealerController;
import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.dto.CreateDealerDTO;
import com.ca.agency.car.seller.dto.UpdateDealerDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.service.DealerService;

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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.ca.agency.car.seller.carseller.controller.JsonUtil.asJsonString;

@ExtendWith(MockitoExtension.class)
public class DealerControllerTests {
    @Mock
    private DealerService serviceMock;
    
    @InjectMocks
    private DealerController dealerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(dealerController)
                .build();
    }
    
    @Test
    @DisplayName("It should Created Dealer when do /api/dealer POST with valid payload")
    public void shouldCreateDealer() throws Exception{
        var createDealerRequest = new CreateDealerDTO("ValidName", 1, "Valid email", "Valid phone");

        var dealerResponse = new Dealer();
        long validDealerId = 1;
        dealerResponse.setId(validDealerId);
        Mockito.when(serviceMock.createDealer(any())).thenReturn(dealerResponse);

        mockMvc.perform( MockMvcRequestBuilders
            .post("/api/dealer")
            .content(asJsonString(createDealerRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    
    @Test
    @DisplayName("It should fail to create Dealer when serviceException is thrown")
    public void shouldFailCreateDealer() throws Exception{
        var createDealerRequest = new CreateDealerDTO("ValidName", 1, "Valid email", "Valid phone");

        var dealerResponse = new Dealer();
        long validDealerId = 1;
        dealerResponse.setId(validDealerId);
        Mockito.when(serviceMock.createDealer(any())).thenThrow(new ServiceException(HttpStatus.CONFLICT, "Errror message"));

        mockMvc.perform( MockMvcRequestBuilders
            .post("/api/dealer")
            .content(asJsonString(createDealerRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("It should update Dealer when do /api/dealer PUT with valid payload")
    public void shouldUpdateDealer() throws Exception{
        long validDealerId = 1;
        var updateDealerRequest = new UpdateDealerDTO("ValidName", 1, "Valid email", "Valid phone");
        updateDealerRequest.setId(validDealerId);
        var dealerResponse = new Dealer();
        dealerResponse.setId(validDealerId);
        Mockito.when(serviceMock.updateDealer(any())).thenReturn(dealerResponse);

        mockMvc.perform( MockMvcRequestBuilders
            .put("/api/dealer")
            .content(asJsonString(updateDealerRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
   
    @Test
    @DisplayName("It should fail to update Dealer when serviceException is thrown")
    public void shouldFailUpdateDealer() throws Exception{
        var updateDealerRequest = new UpdateDealerDTO("ValidName", 1, "Valid email", "Valid phone");

        var dealerResponse = new Dealer();
        long validDealerId = 1;
        dealerResponse.setId(validDealerId);
        Mockito.when(serviceMock.updateDealer(any())).thenThrow(new ServiceException(HttpStatus.CONFLICT, "Errror message"));

        mockMvc.perform( MockMvcRequestBuilders
            .put("/api/dealer")
            .content(asJsonString(updateDealerRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("It should list Dealers when valid payload")
    public void listDealers() throws Exception{
        long validDealerId = 1;
    
        var validDealer = new Dealer("ValidName", 3, "ValidEmail", "ValidPhone");
        validDealer.setId(validDealerId);
        List<Dealer> listListingResponse = new ArrayList<>();
        listListingResponse.add(validDealer);

        Mockito.when(serviceMock.listDealers()).thenReturn(listListingResponse);

        mockMvc.perform( MockMvcRequestBuilders
            .get("/api/dealer")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }
}
