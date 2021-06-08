package com.ca.agency.car.seller.dto;

import io.swagger.annotations.ApiModelProperty;

public class QuestionDTO {
    
    @ApiModelProperty(value = "Unique Identifier of listing", example = "1")
    private long listing;
    @ApiModelProperty(value = "Name of the person interesting in more information about the listing", example = "Hector Bedoya O")
    private String from;
    @ApiModelProperty(value = "The information the person in interest in", example = "Can I do a test run?")
    private String message;
    
    public QuestionDTO() {
        super();
    }

    public QuestionDTO(long listing, String from, String message) {
        this.listing = listing;
        this.from = from;
        this.message = message;
    }

    public long getListing() {
        return listing;
    }

    public void setListing(long listing) {
        this.listing = listing;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
