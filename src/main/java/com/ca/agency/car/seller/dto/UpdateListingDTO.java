package com.ca.agency.car.seller.dto;

import io.swagger.annotations.ApiModelProperty;


public class UpdateListingDTO {

    @ApiModelProperty(value = "Unique identification the listing to update.", example = "1")
    private long id;
    @ApiModelProperty(value = "the value which the dealer consider for a car", example = "4500")
    private Double price;
    @ApiModelProperty(value = "The brand of the car", example = "Chevrolett")
    private String brand;
    @ApiModelProperty(value = "The year of the car", example = "2018")
    private String model;

    public UpdateListingDTO() {
        super();
    }

    public UpdateListingDTO(long id, Double price, String brand, String model) {
        this.id = id;
        this.price = price;
        this.brand = brand;
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
