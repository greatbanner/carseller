package com.ca.agency.car.seller.repository;

import com.ca.agency.car.seller.domain.UnpublishReason;

import org.springframework.data.repository.CrudRepository;

public interface IUnpulishReasonDAO extends CrudRepository<UnpublishReason, Long> {
    
}
