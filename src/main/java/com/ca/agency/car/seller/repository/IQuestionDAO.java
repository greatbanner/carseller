package com.ca.agency.car.seller.repository;

import com.ca.agency.car.seller.domain.Question;

import org.springframework.data.repository.CrudRepository;

public interface IQuestionDAO extends CrudRepository<Question, Long> {
    
}
