package com.ca.agency.car.seller.dto;

import io.swagger.annotations.ApiModelProperty;

public class UpdateDealerDTO {
    
    @ApiModelProperty(value = "Unique identifier of the dealer", example = "1")
    private Long id;

    @ApiModelProperty(value = "Name of the Dealer", example = "RentalCars")
    private String name;
    
    @ApiModelProperty(value = "The number of published listings the dealer can have online.", example = "4")
    private Integer tier;

    @ApiModelProperty(value = "The email address of the Dealer", example = "contact@rentalcars.com")
    private String email;

    @ApiModelProperty(value = "The contact number of the Dealer", example = "514-509-6995")
    private String phone;

    public UpdateDealerDTO() {
        super();
    }

    public UpdateDealerDTO(String name, Integer tier, String email, String phone) {
        this.name = name;
        this.tier = tier;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
