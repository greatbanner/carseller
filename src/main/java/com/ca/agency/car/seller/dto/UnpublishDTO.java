package com.ca.agency.car.seller.dto;

public class UnpublishDTO {

    private String reason;
    private long listing;
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
