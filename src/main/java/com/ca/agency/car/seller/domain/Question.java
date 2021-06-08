package com.ca.agency.car.seller.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Question {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "by")
    private String from;
    private String message;
    private LocalDateTime postingDate;
    private LocalDateTime responseDate;
    private String response;

    @ManyToOne
    @JoinColumn(name = "listing", referencedColumnName = "id")
    private Listing listing;
    
    public Question() {
        super();
    }

    public Question(String from, String message, Listing listing, LocalDateTime postingDate) {
        this.from = from;
        this.message = message;
        this.listing = listing;
        this.postingDate = postingDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public LocalDateTime getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDateTime postingDate) {
        this.postingDate = postingDate;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    
}
