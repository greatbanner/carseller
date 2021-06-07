package com.ca.agency.car.seller.carseller.controller;

import com.ca.agency.car.seller.controller.ListingController;
import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.domain.Listing;
import com.ca.agency.car.seller.domain.ListingState;
import com.ca.agency.car.seller.dto.PublishListingDTO;
import com.ca.agency.car.seller.dto.UnpublishDTO;
import com.ca.agency.car.seller.dto.createListingDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.service.ManageListingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ListingControllerTests {

    @Mock
    private ManageListingService serviceMock;
    
    @InjectMocks
    private ListingController listingController;

    @InjectMocks
    private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(listingController)
                .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
                .build();
    }

    @Test
    @DisplayName("It should response Created when do api/listing POST with valid payload")
    public void createListing() throws Exception{
        var validPrice = 3500.0;
        var validBrand = "Brand";
        var validModel = "Model";
        var createListingRequest = new createListingDTO(1, validPrice, validBrand, validModel);

        var listingResponse = new Listing(new Dealer(), validPrice, ListingState.DRAFT, validBrand, validModel);
        long validListingId = 1;
        listingResponse.setId(validListingId);
        Mockito.when(serviceMock.createListing(any())).thenReturn(listingResponse);

        mockMvc.perform( MockMvcRequestBuilders
            .post("/api/listing")
            .content(asJsonString(createListingRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("It should response BadRequest when do api/listing POST with invalid payload")
    public void createListingfail() throws Exception{
        var createListingRequest = new createListingDTO(1, 3500.0, "validBrand", "validModel");

        Mockito.when(serviceMock.createListing(any())).thenThrow(new ServiceException(HttpStatus.BAD_REQUEST, "exception message"));

        mockMvc.perform( MockMvcRequestBuilders
            .post("/api/listing")
            .content(asJsonString(createListingRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should publish Listing when do api/listing/publish POST with valid payload")
    public void publishListing() throws Exception{
        long validDealerId = 1;
        long validListingId = 1;
        var publishRequest = new PublishListingDTO(validDealerId, validListingId);

        var listingResponse = new Listing(new Dealer(), 3500.0, ListingState.PUBLISHED, "validBrand", "validModel");
        listingResponse.setId(validListingId);

        Mockito.when(serviceMock.publishListing(any())).thenReturn(listingResponse);

        mockMvc.perform( MockMvcRequestBuilders
        .post("/api/listing/publish")
        .content(asJsonString(publishRequest))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("It should fail to publish Listing when do api/listing/publish POST with invalid payload")
    public void publishListingFail() throws Exception{
        long validDealerId = 1;
        long validListingId = 1;
        var publishRequest = new PublishListingDTO(validDealerId, validListingId);

        Mockito.when(serviceMock.publishListing(any())).thenThrow(new ServiceException(HttpStatus.CONFLICT, "error message"));

        mockMvc.perform( MockMvcRequestBuilders
        .post("/api/listing/publish")
        .content(asJsonString(publishRequest))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
    }
    
    @Test
    @DisplayName("It should unpublish Listing when do api/listing/unpublish POST with valid payload")
    public void unpublishListing() throws Exception{
        long validDealerId = 1;
        long validListingId = 1;
        var unpublishRequest = new UnpublishDTO("valid reason", validDealerId, validListingId);

        var listingResponse = new Listing(new Dealer(), 3500.0, ListingState.DRAFT, "validBrand", "validModel");
        listingResponse.setId(validListingId);

        Mockito.when(serviceMock.unpublishListing(any())).thenReturn(listingResponse);

        mockMvc.perform( MockMvcRequestBuilders
        .post("/api/listing/unpublish")
        .content(asJsonString(unpublishRequest))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print()) 
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("It should fail to unpublish Listing when do api/listing/unpublish POST with invalid payload")
    public void unpublishListingFail() throws Exception{
        long validDealerId = 1;
        long validListingId = 1;
        var unpublishRequest = new UnpublishDTO("valid reason", validDealerId, validListingId);

        Mockito.when(serviceMock.unpublishListing(any())).thenThrow(new ServiceException(HttpStatus.CONFLICT, "message"));

        mockMvc.perform( MockMvcRequestBuilders
        .post("/api/listing/unpublish")
        .content(asJsonString(unpublishRequest))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}    
  