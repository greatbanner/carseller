package com.ca.agency.car.seller.repository;

import java.util.List;

import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.domain.Listing;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IListingDAO extends CrudRepository<Listing, Long>{
    
    List<Listing> findByDealer(Dealer dealer);

    @Query(
    value = "SELECT COUNT(l.id) FROM LISTING l WHERE l.state = 'PUBLISHED' and l.dealer = ?1", 
    nativeQuery = true)
    int countPublishedByDealer(long dealerId);
}
