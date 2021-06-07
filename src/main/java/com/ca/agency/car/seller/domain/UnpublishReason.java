package com.ca.agency.car.seller.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class UnpublishReason {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String reason;
    @ManyToOne
    @JoinColumn(name = "listing", referencedColumnName = "id")
    private Listing listing;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate unpostingDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate postingDate;
    
    public UnpublishReason() {
    }

    public UnpublishReason( String reason, Listing listing, LocalDate unpostingDate, LocalDate postingDate) {
        this.reason = reason;
        this.listing = listing;
        this.unpostingDate = unpostingDate;
        this.postingDate = postingDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }
    
    public LocalDate getUnpostingDate() {
        return unpostingDate;
    }

    public void setUnpostingDate(LocalDate unpostingDate) {
        this.unpostingDate = unpostingDate;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDate postingDate) {
        this.postingDate = postingDate;
    }
    
}
