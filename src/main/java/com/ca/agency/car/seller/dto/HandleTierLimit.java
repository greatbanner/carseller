package com.ca.agency.car.seller.dto;

public enum HandleTierLimit {
    LIMIT_REACHED("LIMIT_REACHED"),
    UNPUBLISH_OLDEST("UNPUBLISH_OLDEST"),
    NONE("NONE");

    public final String label;

    private HandleTierLimit(String label) {
        this.label = label;
    }

}
