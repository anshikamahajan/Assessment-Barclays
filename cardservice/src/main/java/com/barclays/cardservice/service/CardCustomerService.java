package com.barclays.cardservice.service;

import java.util.List;

import com.barclays.cardservice.dto.CardDTO;
import com.barclays.cardservice.dto.CustomerCardsDTO;
import com.barclays.cardservice.dto.CustomerDTO;
import com.barclays.cardservice.exception.BarclaysException;

/**
 * CardCustomerService - Interface for card customer service api
 * @author Anshika
 *
 */
public interface CardCustomerService {
	public CustomerCardsDTO getCustomerDetails(Integer customerId) throws BarclaysException;
	public Integer addCustomer(CustomerDTO customerDTO) throws BarclaysException;
	public void issueCardToExistingCustomer(Integer customerId, CardDTO cardDTO) throws BarclaysException;
	public void deleteCustomer(Integer customerId) throws BarclaysException;
	public void deleteCardOfExistingCustomer(Integer customerId, List<Integer> cardIdsToDelete) throws BarclaysException;	
		
}

