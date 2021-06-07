package com.ca.agency.car.seller.service;

import java.time.LocalDate;
import java.util.List;

import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.domain.Listing;
import com.ca.agency.car.seller.domain.ListingState;
import com.ca.agency.car.seller.domain.UnpublishReason;
import com.ca.agency.car.seller.dto.createListingDTO;
import com.ca.agency.car.seller.dto.PublishListingDTO;
import com.ca.agency.car.seller.dto.UnpublishDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.repository.IDealerDAO;
import com.ca.agency.car.seller.repository.IListingDAO;
import com.ca.agency.car.seller.repository.IUnpulishReasonDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ManageListingService {
    
    private final IListingDAO listingDao;
    private final IDealerDAO dealerDao;
    private final IUnpulishReasonDAO unpublishReasonDao;

    @Autowired
    public ManageListingService(IListingDAO listingDao, IDealerDAO dealerDao, IUnpulishReasonDAO unpublishReasonDao){
        this.listingDao = listingDao;
        this.dealerDao = dealerDao;
        this.unpublishReasonDao = unpublishReasonDao;
    }

    public Listing createListing(createListingDTO listingData) throws ServiceException {
        
        var dealer = findDealer(listingData.getDealer());

        var listing = new Listing(dealer, listingData.getPrice(),
                            ListingState.DRAFT, listingData.getBrand(), listingData.getModel());

        return listingDao.save(listing);
    }

    public Listing publishListing(PublishListingDTO publishRequest) throws ServiceException{
        var dealer = findDealer(publishRequest.getDealer());
       
        if( dealer.getTier() == listingDao.countPublishedByDealer(publishRequest.getDealer()) ) {
            throw new ServiceException( HttpStatus.CONFLICT,
                "The Dealer has reached his Tier limit"
            );
        }

        Listing listingToPublish = findListing(publishRequest.getListing());
        validateListingfromDealer(dealer, listingToPublish);
        listingToPublish.setPostingDate(LocalDate.now());                                 
        listingToPublish.setState(ListingState.PUBLISHED);
        return listingDao.save(listingToPublish);
    }

    public Listing unpublishListing(UnpublishDTO unPublishRequest) throws ServiceException{
        var listingToUnpublish = findListing(unPublishRequest.getListing());
        var dealer = findDealer(unPublishRequest.getDealer());
        validateListingfromDealer(dealer, listingToUnpublish);
        var unpublishReason = new UnpublishReason(unPublishRequest.getReason(), listingToUnpublish, LocalDate.now(), listingToUnpublish.getPostingDate());
        listingToUnpublish.setPostingDate(null);                                                    
        listingToUnpublish.setState(ListingState.DRAFT);
        unpublishReasonDao.save(unpublishReason);
        return listingDao.save(listingToUnpublish);
    }

    public List<Listing> listLisings(String sellerName){
        var dealer= dealerDao.findByName(sellerName);
        return listingDao.findByDealer(dealer);
    }

    private Dealer findDealer(long dealerID) throws ServiceException{
        var dealer = dealerDao.findById(dealerID)
        .orElseThrow(() -> new ServiceException( HttpStatus.BAD_REQUEST,
                    new  StringBuilder("Dealer with ID ")
                    .append(dealerID+"")
                    .append(" not found. Please verify").toString()));
        return dealer;
    }

    private Listing  findListing(long listingID) throws ServiceException {
        var listing = listingDao.findById(listingID).orElseThrow(() -> new ServiceException( HttpStatus.BAD_REQUEST,
                    new  StringBuilder("Listing with ID ")
                    .append(listingID+"")
                    .append(" not found. Please verify").toString()));
        return listing;
    }

    private void validateListingfromDealer(Dealer dealer, Listing listing) throws ServiceException{
        if( dealer.getId() != listing.getDealer().getId() ){
            throw new ServiceException( HttpStatus.CONFLICT,
                    new  StringBuilder("Dealer with ID: ")
                    .append(dealer.getId() +"")
                    .append(" do not have a Listing with ID: ")
                    .append(listing.getId())
                    .append(". Please verify").toString()
            );
        }
    }
}