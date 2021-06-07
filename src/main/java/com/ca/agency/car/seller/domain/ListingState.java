package com.ca.agency.car.seller.domain;

public enum ListingState {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED");

    public final String label;

    private ListingState(String label) {
        this.label = label;
    }
}
