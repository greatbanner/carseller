package com.ca.agency.car.seller.dto;

import io.swagger.annotations.ApiModelProperty;

public class PublishListingDTO {

    @ApiModelProperty(value = "Owner of the listing", example = "1")
    private long dealer;
    @ApiModelProperty(value = "The unique identifier of the targeted listing to publish", example = "1")
    private long listing;
    @ApiModelProperty(value = "Defines how to handle if Dealer Tier Limit has been reached by eighter failing with error or unpublishing the oldest listing", example = "UNPUBLISH_OLDEST")
    private HandleTierLimit handleTierLimit;

    public PublishListingDTO() {
        super();
    }

    public PublishListingDTO(long dealer, long listing) {
        this.dealer = dealer;
        this.listing = listing;
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

    public HandleTierLimit getHandleTierLimit() {
        return handleTierLimit;
    }

    public void setHandleTierLimit(HandleTierLimit handleTierLimit) {
        this.handleTierLimit = handleTierLimit;
    }

}
