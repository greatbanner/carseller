package com.ca.agency.car.seller.repository;


import java.util.Optional;

import com.ca.agency.car.seller.domain.Dealer;

import org.springframework.data.repository.CrudRepository;

public interface IDealerDAO extends CrudRepository<Dealer, Long>{

    Dealer findByName(String name);

    Optional<Dealer> findByEmail(String email);
}
