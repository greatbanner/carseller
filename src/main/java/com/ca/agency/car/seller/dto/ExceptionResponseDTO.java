package com.ca.agency.car.seller.dto;

import io.swagger.annotations.ApiModelProperty;

public class ExceptionResponseDTO {

    @ApiModelProperty(value = "Timestamp of the error response", example = "2021-06-04T15:53:12.667+00:00")
    private String timestamp;

    @ApiModelProperty(value = "HTTP Status code of the error", example = "404")
    private String status;

    @ApiModelProperty(value = "Error message", example = "Not found")
    private String error;

    @ApiModelProperty(value = "Detailed message of the error", example = "Dealer with ID 1 not found. Please verify")
    private String message;

    @ApiModelProperty(value = "Path of the requesting resource", example = "/api/v1/listing")
    private String path;

    public ExceptionResponseDTO() {
        super();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    
}
