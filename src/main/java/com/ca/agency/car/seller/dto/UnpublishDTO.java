package com.ca.agency.car.seller.dto;

public class UnpublishDTO {

    private String reason;
    private long listing;
    private long dealer;
    
    public UnpublishDTO() {
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
