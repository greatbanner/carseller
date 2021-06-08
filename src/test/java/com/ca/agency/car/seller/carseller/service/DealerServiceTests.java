package com.ca.agency.car.seller.carseller.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ca.agency.car.seller.domain.Dealer;
import com.ca.agency.car.seller.dto.CreateDealerDTO;
import com.ca.agency.car.seller.dto.UpdateDealerDTO;
import com.ca.agency.car.seller.exception.ServiceException;
import com.ca.agency.car.seller.repository.IDealerDAO;
import com.ca.agency.car.seller.service.DealerService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class DealerServiceTests {
    
    @Mock
    private IDealerDAO dealerDaoMock;

    @InjectMocks
    private DealerService dealerService;

    @Test
    @DisplayName("It should create Dealer when request is valid")
    public void shouldCreateDealer() throws ServiceException{
        //Given:
        var validEmail = "Valid email";
        var validDealerRequest = new CreateDealerDTO("name", 2, validEmail, "phone");
        var validDealer = getValidDealer();
        //When:
        Mockito.when(dealerDaoMock.findByEmail(validEmail)).thenReturn(Optional.empty());
        Mockito.when(dealerDaoMock.save(any())).thenReturn(validDealer);
        var response = dealerService.createDealer(validDealerRequest);
        //Then:
        Assertions.assertThatNoException();
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("It should fail to create Dealer when email already in use")
    public void shouldFailCreateDealerWhenEmailInUse() {
        //Given:
        var emailInUse = "Email in use";
        var validDealerRequest = new CreateDealerDTO("name", 2, emailInUse, "phone");
        //When:
        Mockito.when(dealerDaoMock.findByEmail(emailInUse)).thenReturn(Optional.of(new Dealer()));
        try {
            dealerService.createDealer(validDealerRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            Assertions.assertThat(e.getMessage()).isEqualTo("Email already in use");
        }
    }

    @Test
    @DisplayName("It should update Dealer when request is valid")
    public void shouldUpdateDealer() throws ServiceException{
        //Given:
        long validDealerId = 1;
        var validEmail = "Valid email";
        var validDealerRequest = new UpdateDealerDTO("name", 2, validEmail, "phone");
        validDealerRequest.setId(validDealerId);
        var validDealer = getValidDealer();
        //When:
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        //Mockito.when(dealerDaoMock.findByEmail(validEmail)).thenReturn(Optional.empty());
        Mockito.when(dealerDaoMock.save(any())).thenReturn(validDealer);
        var response = dealerService.updateDealer(validDealerRequest);
        //Then:
        Assertions.assertThatNoException();
        Assertions.assertThat(response).isNotNull();
    }
 
    @Test
    @DisplayName("It should fail to update Dealer when dealer not found")
    public void shouldFailUpdateDealerWhenDealerNotFound() {
        //Given:
        long invalidDealerId = 1;
        var validEmail = "Valid email";
        var validDealerRequest = new UpdateDealerDTO("name", 2, validEmail, "phone");
        validDealerRequest.setId(invalidDealerId);
        //When:
        Mockito.when(dealerDaoMock.findById(invalidDealerId)).thenReturn(Optional.empty());
        try {
            dealerService.updateDealer(validDealerRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(e.getMessage()).isEqualTo(new  StringBuilder("Dealer with ID ")
            .append(invalidDealerId+"")
            .append(" not found. Please verify").toString());
        }
    }

    @Test
    @DisplayName("It should fail to update Dealer when new email is already in use")
    public void shouldFailUpdateDealerWhenEmailInUse() {
        //Given:
        long validDealerId = 1;
        var invalidEmail = "Email in use";
        var validDealerRequest = new UpdateDealerDTO("name", 2, invalidEmail, "phone");
        validDealerRequest.setId(validDealerId);
        var validDealer = getValidDealer();
        //When:
        Mockito.when(dealerDaoMock.findById(validDealerId)).thenReturn(Optional.of(validDealer));
        Mockito.when(dealerDaoMock.findByEmail(invalidEmail)).thenReturn(Optional.of(new Dealer()));
        try {
            dealerService.updateDealer(validDealerRequest);
        } catch (ServiceException e) {
            //Then:
            Assertions.assertThat(e.getHttpStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            Assertions.assertThat(e.getMessage()).isEqualTo("Email already in use");
        }
    }

    @Test
    @DisplayName("It should list Dealers when request is valid")
    public void shouldListDealer() throws ServiceException{
        //Given:
        var validDealer = getValidDealer();
        List<Dealer> listDealerResponse = new ArrayList<>();
        listDealerResponse.add(validDealer);
        //When:
        Mockito.when(dealerDaoMock.findAll()).thenReturn(listDealerResponse);
        var response = dealerService.listDealers();
        //Then:
        Assertions.assertThatNoException();
        Assertions.assertThat(response).isNotEmpty();
    }

    private Dealer getValidDealer(){
        return new Dealer("Valid Name", 2, "Valid Email", "Valid Phone");
    }
}
