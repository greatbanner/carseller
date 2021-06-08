package com.ca.agency.car.seller.controller;

import java.util.List;

import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.dto.CreateDealerDTO;
import com.ca.agency.car.seller.dto.ExceptionResponseDTO;
import com.ca.agency.car.seller.dto.UpdateDealerDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.service.DealerService;

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
import org.springframework.web.bind.annotation.GetMapping;


@RestController()
@RequestMapping("/api/dealer")
@Api(value = "Dealer", tags = { "Dealer" })
public class DealerController {

    private final DealerService dealerService;

    @Autowired
    public DealerController(DealerService dealerService){
        this.dealerService = dealerService;
    }

    @PostMapping( consumes = "application/json", produces = "application/json")
    @ApiOperation("Helps you to create a Dealer")
    @ApiResponses({
        @ApiResponse( code = 201, message = "Created", response = Dealer.class ),
        @ApiResponse( code = 404, message = "Listing not found", response = ExceptionResponseDTO.class )
    })
    public ResponseEntity<Dealer> createDealer(@RequestBody CreateDealerDTO dealerRequest) {
        Dealer dealer;
        try {
            dealer = dealerService.createDealer(dealerRequest);
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                e.getHttpStatusCode(), e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(dealer);
    }
    
    @PutMapping( consumes = "application/json", produces = "application/json")
    @ApiOperation("Helps you to update the Dealer data")
    @ApiResponses({
        @ApiResponse( code = 200, message = "Ok", response = Dealer.class ),
        @ApiResponse( code = 404, message = "Listing not found", response = ExceptionResponseDTO.class )
    })
    public ResponseEntity<Dealer> updateDealer(@RequestBody UpdateDealerDTO dealerRequest) {
        Dealer dealer;
        try {
            dealer = dealerService.updateDealer(dealerRequest);
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                e.getHttpStatusCode(), e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dealer);
    }

    @GetMapping()
    public List<Dealer> listDealers() {
        return dealerService.listDealers();
    }
    
}
