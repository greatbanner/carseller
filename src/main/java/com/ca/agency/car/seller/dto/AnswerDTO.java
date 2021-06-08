package com.ca.agency.car.seller.dto;

import io.swagger.annotations.ApiModelProperty;

public class AnswerDTO {
    
    @ApiModelProperty(value = "Unique Identifier of a Question", example = "1")
    private long question;
    @ApiModelProperty(value = "Response message", example = "Yes, we can set a date to do the Test Run")
    private String response;
    
    public AnswerDTO() {
        super();
    }

    public AnswerDTO(long question, String response) {
        this.question = question;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getQuestion() {
        return question;
    }

    public void setQuestion(long question) {
        this.question = question;
    }

}
