package com.barclays.cardservice.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.cardservice.constants.SystemConstants;
import com.barclays.cardservice.dto.CardDTO;
import com.barclays.cardservice.dto.CustomerCardsDTO;
import com.barclays.cardservice.dto.CustomerDTO;
import com.barclays.cardservice.exception.BarclaysException;
import com.barclays.cardservice.service.CardCustomerService;

/**
 * Cust_Controller - Rest api for customer details related routes
 * @author Anshika
 * 
 */
@RestController
@RequestMapping("/api")
public class Cust_Controller {
	
	@Autowired
	CardCustomerService cardCustomerService;
	
	/**
	 * getCustomer - Get all details of customer including card details
	 * @param customerId - Id of customer
	 * @return Customer Details json
	 * @throws BarclaysException
	 */
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<CustomerCardsDTO> getCustomer(@PathVariable Integer customerId) throws BarclaysException {
		CustomerCardsDTO customer = cardCustomerService.getCustomerDetails(customerId);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}
	
	/**
	 * addCustomer - Create new customer
	 * @param customer - customer data
	 * @return ID of newly created customer
	 * @throws BarclaysException
	 */
	@PostMapping("/customer/new")
	public ResponseEntity<Integer> addCustomer(@RequestBody CustomerDTO customer) throws BarclaysException {
		Integer id = cardCustomerService.addCustomer(customer);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	/**
	 * issueCard - Issues new card to a user
	 * @param id - Customer id
	 * @param card - Details for new card
	 * @return Customer response
	 * @throws BarclaysException
	 */
	@PostMapping("/customer/{id}/newcard")
	public ResponseEntity<String> issueCard(@PathVariable Integer id, @RequestBody CardDTO card) throws BarclaysException {
		cardCustomerService.issueCardToExistingCustomer(id, card);
		return new ResponseEntity<>(SystemConstants.CARD_ISSUSED_SUCCESS_RESPONSE, HttpStatus.OK);
	}
	
	@DeleteMapping("/customer/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) throws BarclaysException {
		cardCustomerService.deleteCustomer(id);
		return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/customer/{id}/cards")
	public ResponseEntity<String> deleteCustomerCards(@PathVariable Integer id, @RequestBody List<Integer> cardIds) throws BarclaysException {
		cardCustomerService.deleteCardOfExistingCustomer(id, cardIds);
		return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
	}
}
