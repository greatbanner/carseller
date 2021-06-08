package com.ca.agency.car.seller.service;

import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.dto.CreateDealerDTO;
import com.ca.agency.car.seller.dto.UpdateDealerDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.repository.IDealerDAO;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DealerService {
    
    private final IDealerDAO dealerDao;

    public DealerService(IDealerDAO dealerDao){
        this.dealerDao = dealerDao;
    }

    public Dealer createDealer(CreateDealerDTO daelerRequest) throws ServiceException{
        if(dealerDao.findByEmail(daelerRequest.getEmail()).isPresent()){
            throw new ServiceException(HttpStatus.CONFLICT, "Email already in use");
        }
        var dealer = new Dealer(daelerRequest.getName(), daelerRequest.getTier(), daelerRequest.getEmail(), daelerRequest.getPhone());
        return dealerDao.save(dealer);
    }

    public Dealer updateDealer(UpdateDealerDTO daelerRequest) throws ServiceException{
        var dealer = dealerDao.findById(daelerRequest.getId())
            .orElseThrow(() -> new ServiceException( HttpStatus.BAD_REQUEST,
                    new  StringBuilder("Dealer with ID ")
                    .append(daelerRequest.getId()+"")
                    .append(" not found. Please verify").toString()));
        
        if(!dealer.getEmail().equals(daelerRequest.getEmail()) && dealerDao.findByEmail(daelerRequest.getEmail()).isPresent()){
            throw new ServiceException(HttpStatus.CONFLICT, "Email already in use");
        } else if(!daelerRequest.getEmail().isEmpty()) {
            dealer.setEmail(daelerRequest.getEmail());
        }
        if(!daelerRequest.getName().isBlank()){
            dealer.setName(daelerRequest.getName());
        }
        if(!daelerRequest.getPhone().isBlank()){
            dealer.setPhone(daelerRequest.getPhone());
        }
        if(daelerRequest.getTier() != null){
            dealer.setTier(daelerRequest.getTier());
        }
        return dealerDao.save(dealer);
    }
}
