package com.ca.agency.car.seller.dto;

import io.swagger.annotations.ApiModelProperty;

public class PublishListingDTO {

    @ApiModelProperty(value = "Owner of the listing", example = "1")
    private long dealer;
    @ApiModelProperty(value = "The unique identifier of the targeted listing to publish", example = "1")
    private long listing;

    public PublishListingDTO() {
    }

    public long getDealer() {
        return dealer;
    }

    public void setDealer(long dealer) {
        this.dealer = dealer;
    }

    public long getListing() {
        return listing;
    }

    public void setListing(long listing) {
        this.listing = listing;
    }

}
