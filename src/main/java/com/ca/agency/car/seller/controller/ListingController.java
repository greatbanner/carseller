package com.ca.agency.car.seller.controller;


import com.ca.agency.car.seller.domain.Listing;
import com.ca.agency.car.seller.domain.ListingState;
import com.ca.agency.car.seller.dto.ExceptionResponseDTO;
import com.ca.agency.car.seller.dto.CreateListingDTO;
import com.ca.agency.car.seller.dto.PublishListingDTO;
import com.ca.agency.car.seller.dto.UnpublishDTO;
import com.ca.agency.car.seller.dto.UpdateListingDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.service.ManageListingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController()
@RequestMapping("/api/listing")
@Api(value = "Listing", tags = { "Listing" })
public class ListingController {

    private final ManageListingService manageListingService;

    @Autowired
    public ListingController(ManageListingService manageListingService){
        this.manageListingService = manageListingService;
    }
    
    @PostMapping( consumes = "application/json", produces = "application/json")
    @ApiOperation("Helps you to create a Vehicule listing with the proper data")
    @ApiResponses({
        @ApiResponse( code = 201, message = "Created", response = CreateListingDTO.class ),
        @ApiResponse( code = 404, message = "Dealer not found", response = ExceptionResponseDTO.class )
    })
    public ResponseEntity<Listing> createListing(@RequestBody CreateListingDTO listingData) {
        Listing listing;
        try {
            listing = manageListingService.createListing(listingData);
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                e.getHttpStatusCode(), e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(listing);
    }

    @PutMapping( consumes = "application/json", produces = "application/json")
    @ApiOperation("Helps you to update a Vehicule listing with the proper data")
    @ApiResponses({
        @ApiResponse( code = 200, message = "Ok", response = CreateListingDTO.class ),
        @ApiResponse( code = 404, message = "Dealer not found", response = ExceptionResponseDTO.class )
    })
    public ResponseEntity<Listing> updateListing(@RequestBody UpdateListingDTO listingData) {
        Listing listing;
        try {
            listing = manageListingService.updateListing(listingData);
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                e.getHttpStatusCode(), e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(listing);
    }

    @PostMapping( path = "/publish", consumes = "application/json", produces = "application/json")
    @ApiOperation("Helps you to publish a listing by the given ID and set the PostingDate to the present time.")
    @ApiResponses({
        @ApiResponse( code = 200, message = "published", response = CreateListingDTO.class ),
        @ApiResponse( code = 404, message = "Dealer not found", response = ExceptionResponseDTO.class )
    })
    public ResponseEntity<Listing> publishListing(@RequestBody PublishListingDTO publishRequest) {
        Listing listing;
        try {
            listing = manageListingService.publishListing(publishRequest);
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                e.getHttpStatusCode(), e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(listing);
    }

    @PostMapping( path = "/unpublish", consumes = "application/json", produces = "application/json")
    @ApiOperation("Helps you to publish a listing by the given ID and set the PostingDate to the present time.")
    @ApiResponses({
        @ApiResponse( code = 200, message = "unpublished", response = CreateListingDTO.class ),
        @ApiResponse( code = 404, message = "Dealer not found", response = ExceptionResponseDTO.class )
    })
    public ResponseEntity<Listing> unpublishListing(@RequestBody UnpublishDTO unpublishRequest) {
        Listing listing;
        try {
            listing = manageListingService.unpublishListing(unpublishRequest);
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                e.getHttpStatusCode(), e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(listing);
    }
     
    @GetMapping( produces = "application/json")
    public Page<Listing> findByDealer(@RequestParam long dealer, @RequestParam ListingState state, @RequestParam( name = "page", required = false) Integer page, 
    @RequestParam(name = "size", required = false) Integer size) {
        Page<Listing> listings;
        try {
            Pageable paging = null;
            if(page != null && size != null){
                paging = PageRequest.of(page, size);
            }
            listings = manageListingService.listLisings(dealer, state, paging);
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                e.getHttpStatusCode(), e.getMessage(), e);
        }
        return listings;
    }


}
