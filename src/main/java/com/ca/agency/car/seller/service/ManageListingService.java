package com.ca.agency.car.seller.service;

import java.time.LocalDate;

import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.domain.Listing;
import com.ca.agency.car.seller.domain.ListingState;
import com.ca.agency.car.seller.domain.UnpublishReason;
import com.ca.agency.car.seller.dto.CreateListingDTO;
import com.ca.agency.car.seller.dto.UpdateListingDTO;
import com.ca.agency.car.seller.dto.PublishListingDTO;
import com.ca.agency.car.seller.dto.UnpublishDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.repository.IDealerDAO;
import com.ca.agency.car.seller.repository.IListingDAO;
import com.ca.agency.car.seller.repository.IUnpulishReasonDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ManageListingService {

    private static final String DEFAULT_UNPUBLISH_REASON = "Tier limit reached";
    
    private final IListingDAO listingDao;
    private final IDealerDAO dealerDao;
    private final IUnpulishReasonDAO unpublishReasonDao;

    @Autowired
    public ManageListingService(IListingDAO listingDao, IDealerDAO dealerDao, IUnpulishReasonDAO unpublishReasonDao){
        this.listingDao = listingDao;
        this.dealerDao = dealerDao;
        this.unpublishReasonDao = unpublishReasonDao;
    }

    public Listing createListing(CreateListingDTO listingData) throws ServiceException {
        
        var dealer = findDealer(listingData.getDealer());

        var listing = new Listing(dealer, listingData.getPrice(),
                            ListingState.DRAFT, listingData.getBrand(), listingData.getModel());

        return listingDao.save(listing);
    }

    public Listing publishListing(PublishListingDTO publishRequest) throws ServiceException{
        var dealer = findDealer(publishRequest.getDealer());
       
        if( dealer.getTier() == listingDao.countPublishedByDealer(publishRequest.getDealer()) ) {
            switch(publishRequest.getHandleTierLimit()){
                case LIMIT_REACHED:
                    throw new ServiceException( HttpStatus.CONFLICT,
                        "The Dealer has reached his Tier limit"
                    );
                case UNPUBLISH_OLDEST:
                    var listingToUnpublish = listingDao.findOldestOnebyDealer(publishRequest.getDealer());
                    var unpublishRequest = new UnpublishDTO(DEFAULT_UNPUBLISH_REASON, listingToUnpublish.getId(), publishRequest.getDealer());
                    unpublishListing(unpublishRequest);
                    break;
                default:
                    throw new ServiceException( HttpStatus.CONFLICT,
                        "The Dealer has reached his Tier limit"
                    );
            }
        }

        var listingToPublish = findListing(publishRequest.getListing());
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

    public Listing updateListing(UpdateListingDTO updateRequest) throws ServiceException{
        var listingToUpdate = findListing(updateRequest.getId());
        if(updateRequest.getBrand() != null){
            listingToUpdate.setBrand(updateRequest.getBrand());
        }
        if(updateRequest.getModel() != null){
            listingToUpdate.setModel(updateRequest.getModel());
        }
        if(updateRequest.getPrice() != null){
            listingToUpdate.setPrice(updateRequest.getPrice());
        }
        return listingDao.save(listingToUpdate);
    }

    public Page<Listing> listLisings( long dealerId, ListingState state, Pageable pageable ) throws ServiceException{
        var dealer = findDealer(dealerId);
        return listingDao.findByStateAndDealer(state, dealer, pageable);
    }

    private Dealer findDealer(long dealerID) throws ServiceException{
        return dealerDao.findById(dealerID)
        .orElseThrow(() -> new ServiceException( HttpStatus.BAD_REQUEST,
                    new  StringBuilder("Dealer with ID ")
                    .append(dealerID+"")
                    .append(" not found. Please verify").toString()));
    }

    private Listing  findListing(long listingID) throws ServiceException {
        return listingDao.findById(listingID).orElseThrow(() -> new ServiceException( HttpStatus.BAD_REQUEST,
                    new  StringBuilder("Listing with ID ")
                    .append(listingID+"")
                    .append(" not found. Please verify").toString()));
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
