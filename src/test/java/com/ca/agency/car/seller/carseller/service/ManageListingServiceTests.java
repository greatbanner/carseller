package com.ca.agency.car.seller.carseller.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.domain.Listing;
import com.ca.agency.car.seller.domain.ListingState;
import com.ca.agency.car.seller.dto.HandleTierLimit;
import com.ca.agency.car.seller.dto.PublishListingDTO;
import com.ca.agency.car.seller.dto.UnpublishDTO;
import com.ca.agency.car.seller.dto.UpdateListingDTO;
import com.ca.agency.car.seller.dto.CreateListingDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.repository.IDealerDAO;
import com.ca.agency.car.seller.repository.IListingDAO;
import com.ca.agency.car.seller.repository.IUnpulishReasonDAO;
import com.ca.agency.car.seller.service.ManageListingService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class ManageListingServiceTests {

    @Mock
    private IListingDAO listingDaoMock;

    @Mock
    private IUnpulishReasonDAO unpublishReasonDaoMock;

    @Mock
    private IDealerDAO dealerDaoMock;

    @InjectMocks
    private ManageListingService manageListingService;

    @Test
    @DisplayName("Listing should be created when a valid request is sent")
    public void shouldCreateListingWhenRequestIsValid() throws ServiceException{
        //Given:
        var validCreateListingRequest = new CreateListingDTO();
        validCreateListingRequest.setBrand("Valid Brand");
        long validDealerId = 1;
        double validPrice = 3500.0;
        validCreateListingRequest.setDealer(validDealerId);
        validCreateListingRequest.setModel("Valid model");
        validCreateListingRequest.setPrice(validPrice);
        var validDealer = getValidDealer(validDealerId);
        long validListingId =  1;

        var validListingResponse = getValidListing(validListingId, validDealer);
        //When:
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        Mockito.when(listingDaoMock.save(any())).thenReturn(validListingResponse);
        Listing createListingResponse = manageListingService.createListing(validCreateListingRequest);
        //Then:
        Assertions.assertThatNoException();
        Assertions.assertThat(createListingResponse.getId()).isNotNull();
        Assertions.assertThat(createListingResponse).isEqualTo(validListingResponse);
    }

    @Test
    @DisplayName("Listing should not be created when the dealer do not exists")
    public void shouldNotCreateListingWhenDealerNotExists() {
        //Given:
        var invalidCreateListingRequest = new CreateListingDTO();
        long invalidDealerId = 1;
        invalidCreateListingRequest.setDealer(invalidDealerId);
        //When:
        Mockito.when(dealerDaoMock.findById(invalidDealerId)).thenReturn(Optional.empty());
        try {
            manageListingService.createListing(invalidCreateListingRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(e.getMessage()).isEqualTo(
                new  StringBuilder("Dealer with ID ")
                    .append(invalidDealerId+"")
                    .append(" not found. Please verify").toString()
            );
        }
    }

    @Test
    @DisplayName("Listing should be published when the request is valid")
    public void shouldPublishListingWhenValidRequest() throws ServiceException{
        //Given:
        var validPublishRequest = new PublishListingDTO();
        long validDealerId = 1;
        long validListingId = 1;
        validPublishRequest.setDealer(validDealerId);
        validPublishRequest.setListing(validListingId);
        
        var validDealer = getValidDealer(validDealerId);
        var validListingResponse = getValidListing(validListingId, validDealer);
        //When:
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        Mockito.when(listingDaoMock.findById(validListingId)).thenReturn(Optional.of(validListingResponse));
        Mockito.when(listingDaoMock.countPublishedByDealer(validDealerId)).thenReturn(validDealer.getTier() -1);
        Mockito.when(listingDaoMock.save(any())).thenAnswer(i -> i.getArguments()[0]);
        var publishedResponse = manageListingService.publishListing(validPublishRequest);
        //Then:
        Assertions.assertThatNoException();
        Assertions.assertThat(publishedResponse.getState()).isEqualTo(ListingState.PUBLISHED);
    }

    @Test
    @DisplayName("It should fail to publish a Listing when Listing is not from the Dealer request")
    public void shouldFailPublishListingWhenListingNotFromSameDealer() {
        //Given:
        var validPublishRequest = new PublishListingDTO();
        long validDealerId = 1;
        long invalidDealerId = 2;
        long validListingId = 1;
        validPublishRequest.setDealer(validDealerId);
        validPublishRequest.setListing(validListingId);
        
        var validDealer = getValidDealer(validDealerId);
        var invalidDealer = getValidDealer(invalidDealerId);
        var validListingResponse = getValidListing(validListingId, invalidDealer);
        //When:
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        Mockito.when(listingDaoMock.findById(validListingId)).thenReturn(Optional.of(validListingResponse));
        Mockito.when(listingDaoMock.countPublishedByDealer(validDealerId)).thenReturn(validDealer.getTier() -1);
        try {
            manageListingService.publishListing(validPublishRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            Assertions.assertThat(e.getMessage()).isEqualTo(new  StringBuilder("Dealer with ID: ")
            .append(validDealerId +"")
            .append(" do not have a Listing with ID: ")
            .append(validListingResponse.getId())
            .append(". Please verify").toString());
        }
    }

    @Test
    @DisplayName("It should fail to publish a Listing when Dealer reached his Tier Limit")
    public void shouldFailPublishListingWhenValidRequest() {
        //Given:
        var validPublishRequest = new PublishListingDTO();
        long validDealerId = 1;
        long validListingId = 1;
        validPublishRequest.setDealer(validDealerId);
        validPublishRequest.setListing(validListingId);
        validPublishRequest.setHandleTierLimit(HandleTierLimit.LIMIT_REACHED);
        var validDealer = getValidDealer(validDealerId);
        var invalidTier = 1;
        //When:
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        Mockito.when(listingDaoMock.countPublishedByDealer(validDealerId)).thenReturn(invalidTier);
        try {
            manageListingService.publishListing(validPublishRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            Assertions.assertThat(e.getMessage()).isEqualTo("The Dealer has reached his Tier limit");
        }
    }

    @Test
    @DisplayName("It should fail to publish a Listing when no handle limit is specified")
    public void shouldFailPublishListingWhenNoHandleTierLimitSpecified() {
        //Given:
        var validPublishRequest = new PublishListingDTO();
        long validDealerId = 1;
        long validListingId = 1;
        validPublishRequest.setDealer(validDealerId);
        validPublishRequest.setListing(validListingId);
        validPublishRequest.setHandleTierLimit(HandleTierLimit.NONE);
        var validDealer = getValidDealer(validDealerId);
        var invalidTier = 1;
        //When:
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        Mockito.when(listingDaoMock.countPublishedByDealer(validDealerId)).thenReturn(invalidTier);
        try {
            manageListingService.publishListing(validPublishRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            Assertions.assertThat(e.getMessage()).isEqualTo("The Dealer has reached his Tier limit");
        }
    }

    @Test
    @DisplayName("It should update listing when updateRequest is valid")
    public void shouldUpdateListingWhenValid() throws ServiceException{
        //Gien:
        long validListingId = 1;
        var validUpdateRequest = new UpdateListingDTO(validListingId, 3500.0, "Updated Brand", "Updated Model", "validShortDescription", "validLongDescription");
        var validListing = getValidListing(validListingId, new Dealer());

        Mockito.when(listingDaoMock.findById(validListingId)).thenReturn(Optional.of(validListing));
        Mockito.when(listingDaoMock.save(validListing)).thenReturn(validListing);
        //When:
        var response = manageListingService.updateListing(validUpdateRequest);
        //Then:
        Assertions.assertThatNoException();
        Assertions.assertThat(response.getBrand()).isEqualTo(validUpdateRequest.getBrand());
        Assertions.assertThat(response.getModel()).isEqualTo(validUpdateRequest.getModel());
    }

    @Test
    @DisplayName("It should fail to publish a Listing when Listing not exists")
    public void shouldFailPublishListingWhenListingNotExists() {
        //Given:
        var validPublishRequest = new PublishListingDTO();
        long validDealerId = 1;
        long invalidListingId = 1;
        validPublishRequest.setDealer(validDealerId);
        validPublishRequest.setListing(invalidListingId);
        
        var validDealer = getValidDealer(validDealerId);
        //When:
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        Mockito.when(listingDaoMock.countPublishedByDealer(validDealerId)).thenReturn(validDealer.getTier()-1);
        Mockito.when(listingDaoMock.findById(invalidListingId)).thenReturn(Optional.empty());
        try {
            manageListingService.publishListing(validPublishRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(e.getMessage()).isEqualTo(new  StringBuilder("Listing with ID ")
            .append(invalidListingId+"")
            .append(" not found. Please verify").toString());
        }
    }
    
    @Test
    @DisplayName("It should publish a Listing when Dealer has reached his tier limit and handle by unpublishing the oldest Listing")
    public void shouldPublishListingWhenLimitHasReachedAndHandleByUnpublishOldest() throws ServiceException {
          //Given:
          var validPublishRequest = new PublishListingDTO();
          long validDealerId = 1;
          long validListingId = 1;
          long listingToUnpublishId = 2;
          validPublishRequest.setDealer(validDealerId);
          validPublishRequest.setListing(validListingId);
          validPublishRequest.setHandleTierLimit(HandleTierLimit.UNPUBLISH_OLDEST);
          
          var validDealer = getValidDealer(validDealerId);
          var validListingResponse = getValidListing(validListingId, validDealer);
          var listingToUnpublish = getValidListing(listingToUnpublishId, validDealer);
          //When:
          Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
          Mockito.when(listingDaoMock.findById(validListingId)).thenReturn(Optional.of(validListingResponse));
          Mockito.when(listingDaoMock.countPublishedByDealer(validDealerId)).thenReturn(validDealer.getTier());
          Mockito.when(listingDaoMock.findOldestOnebyDealer(validPublishRequest.getDealer())).thenReturn(listingToUnpublish);
          Mockito.when(listingDaoMock.findById(listingToUnpublishId)).thenReturn(Optional.of(listingToUnpublish));
          Mockito.when(listingDaoMock.save(any())).thenAnswer(i -> i.getArguments()[0]);
          var publishedResponse = manageListingService.publishListing(validPublishRequest);
          //Then:
          Assertions.assertThatNoException();
          Assertions.assertThat(publishedResponse.getState()).isEqualTo(ListingState.PUBLISHED);
    }

    @Test
    @DisplayName("Should unpublish Listing when a valid request")
    public void shouldUnpublishListinWhenValidRequest() throws ServiceException{
        //Given
        var validUnpublishRequest = new UnpublishDTO();
        long validDealerId = 1;
        long validListingId = 1;
        validUnpublishRequest.setDealer(validDealerId);
        validUnpublishRequest.setListing(validListingId);
        validUnpublishRequest.setReason("valid Reason");

        var validDealer = getValidDealer(validDealerId);
        var validListingResponse = getValidListing(validListingId, validDealer);
        //When:
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        Mockito.when(listingDaoMock.findById(validListingId)).thenReturn(Optional.of(validListingResponse));
        Mockito.when(listingDaoMock.save(any())).thenAnswer(i -> i.getArguments()[0]);
        var unpublishResponse = manageListingService.unpublishListing(validUnpublishRequest);
        //Then
        Assertions.assertThatNoException();
        Assertions.assertThat(unpublishResponse.getState()).isEqualTo(ListingState.DRAFT);
    }

    @Test
    @DisplayName("Should list Listing by seller and state when the seller exists")
    public void shouldListListingWhenValidRequest() throws ServiceException{
        //Given
        var validListingState = ListingState.DRAFT;
        long validDealerId = 1;
        long validListingId = 1;
        var validDealer = getValidDealer(validDealerId);
        var listingResponse = getValidListing(validListingId, validDealer);
        //When

        List<Listing> listListingResponse = new ArrayList<>();
        listListingResponse.add(listingResponse);
        var paging = PageRequest.of(1, 10);
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        Mockito.when(listingDaoMock.findByStateAndDealer(validListingState, validDealer, paging)).thenReturn(new PageImpl<>(listListingResponse));
        var response = manageListingService.listLisings(validDealerId, validListingState, paging);
        //Then
        Assertions.assertThatNoException();
        Assertions.assertThat(response.getContent().size()).isEqualTo(1);
        Assertions.assertThat(listingResponse).isEqualTo(response.getContent().get(0));
    }

    private Dealer getValidDealer(long id){
        var validDealer = new Dealer();
        validDealer.setId(id);
        validDealer.setEmail("validEmail");
        validDealer.setPhone("validPhone");
        validDealer.setName("validName");
        validDealer.setTier(1);
        return validDealer;
    }

    private Listing getValidListing(long id, Dealer dealer){
        var listing = new Listing(dealer, 3500.0, ListingState.PUBLISHED, "validBrand", "validModel", "validShortDescription", "validLongDescription");
        listing.setId(id);
        return listing;
    }
}

