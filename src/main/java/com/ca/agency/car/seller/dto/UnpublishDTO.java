package com.ca.agency.car.seller.dto;

import io.swagger.annotations.ApiModelProperty;

public class UnpublishDTO {

    @ApiModelProperty(value = "Reason why is the listing being unpublish", example = "1")
    private String reason;
    @ApiModelProperty(value = "Unique identifier of the Listing to unpublish", example = "1")
    private long listing;
    @ApiModelProperty(value = "Unique identifier of the dealer owner of the listing", example = "1")
    private long dealer;
    
    public UnpublishDTO() {
        super();
    }

    public UnpublishDTO(String reason, long listing, long dealer) {
        this.reason = reason;
        this.listing = listing;
        this.dealer = dealer;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getListing() {
        return listing;
    }

    public void setListing(long listing) {
        this.listing = listing;
    }

    public long getDealer() {
        return dealer;
    }

    public void setDealer(long dealer) {
        this.dealer = dealer;
    }
    
}
