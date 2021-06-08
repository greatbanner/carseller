package com.ca.agency.car.seller.carseller.controller;

import com.ca.agency.car.seller.controller.ListingController;
import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.domain.Listing;
import com.ca.agency.car.seller.domain.ListingState;
import com.ca.agency.car.seller.dto.PublishListingDTO;
import com.ca.agency.car.seller.dto.UnpublishDTO;
import com.ca.agency.car.seller.dto.UpdateListingDTO;
import com.ca.agency.car.seller.dto.CreateListingDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.service.ManageListingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.ca.agency.car.seller.carseller.controller.JsonUtil.asJsonString;

import java.util.ArrayList;
import java.util.List;

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
        var createListingRequest = new CreateListingDTO(1, validPrice, validBrand, validModel);

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
    @DisplayName("It should response Updated when do api/listing PUT with valid payload")
    public void updateListing() throws Exception{
        var validPrice = 3500.0;
        var validBrand = "Brand";
        var validModel = "Model";
        var updateListingRequest = new UpdateListingDTO(1, validPrice, validBrand, validModel);

        var listingResponse = new Listing(new Dealer(), validPrice, ListingState.DRAFT, validBrand, validModel);
        long validListingId = 1;
        listingResponse.setId(validListingId);
        Mockito.when(serviceMock.updateListing(any())).thenReturn(listingResponse);

        mockMvc.perform( MockMvcRequestBuilders
            .put("/api/listing")
            .content(asJsonString(updateListingRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    
    @Test
    @DisplayName("It should response BadRequest when do api/listing PUT with valid payload")
    public void updateListingFail() throws Exception{
        var validPrice = 3500.0;
        var validBrand = "Brand";
        var validModel = "Model";
        var updateListingRequest = new UpdateListingDTO(1, validPrice, validBrand, validModel);

        Mockito.when(serviceMock.updateListing(any())).thenThrow(new ServiceException(HttpStatus.BAD_REQUEST,"Error message"));

        mockMvc.perform( MockMvcRequestBuilders
            .put("/api/listing")
            .content(asJsonString(updateListingRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should response BadRequest when do api/listing POST with invalid payload")
    public void createListingfail() throws Exception{
        var createListingRequest = new CreateListingDTO(1, 3500.0, "validBrand", "validModel");

        Mockito.when(serviceMock.createListing(any())).thenThrow(new ServiceException(HttpStatus.BAD_REQUEST, "Error message"));

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

        Mockito.when(serviceMock.publishListing(any())).thenThrow(new ServiceException(HttpStatus.CONFLICT, "Error message"));

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
    
    @Test
    @DisplayName("It should list Listing when valid payload")
    public void listListing() throws Exception{
        long validDealerId = 1;
        var validState = ListingState.DRAFT;
    
        var listingResponse = new Listing(new Dealer(), 3450.0, ListingState.DRAFT, "validBrand", "validModel");
        long validListingId = 1;
        listingResponse.setId(validListingId);
        List<Listing> listListingResponse = new ArrayList<>();
        listListingResponse.add(listingResponse);

        Mockito.when(serviceMock.listLisings(validDealerId, validState, null )).thenReturn(new PageImpl<>(listListingResponse));

        mockMvc.perform( MockMvcRequestBuilders
            .get("/api/listing")
            .queryParam("dealer", validDealerId +"")
            .queryParam("state", validState.label)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)));
    }
    
    @Test
    @DisplayName("It should list Listing when valid payload with pagination")
    public void listListingWithPagination() throws Exception{
        long validDealerId = 1;
        var validState = ListingState.DRAFT;
    
        var listingResponse = new Listing(new Dealer(), 3450.0, ListingState.DRAFT, "validBrand", "validModel");
        long validListingId = 1;
        listingResponse.setId(validListingId);
        List<Listing> listListingResponse = new ArrayList<>();
        listListingResponse.add(listingResponse);

        Mockito.when(serviceMock.listLisings(anyLong(), any(), any())).thenReturn(new PageImpl<>(listListingResponse));

        mockMvc.perform( MockMvcRequestBuilders
            .get("/api/listing")
            .queryParam("dealer", validDealerId +"")
            .queryParam("state", validState.label)
            .queryParam("page", "1")
            .queryParam("size", "2")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("It should fail to list Listing when service exception is thrown")
    public void listListingFail() throws Exception{
        long validDealerId = 1;
        var validState = ListingState.DRAFT;

        Mockito.when(serviceMock.listLisings(validDealerId, validState, null )).thenThrow(new ServiceException(HttpStatus.CONFLICT,"Error message"));

        mockMvc.perform( MockMvcRequestBuilders
            .get("/api/listing")
            .queryParam("dealer", validDealerId +"")
            .queryParam("state", validState.label)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

}    
  
