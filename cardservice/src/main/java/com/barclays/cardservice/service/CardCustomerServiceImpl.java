package com.barclays.cardservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barclays.cardservice.dto.CardDTO;
import com.barclays.cardservice.dto.CustomerCardsDTO;
import com.barclays.cardservice.dto.CustomerDTO;
import com.barclays.cardservice.entity.Card;
import com.barclays.cardservice.entity.Customer;
import com.barclays.cardservice.exception.BarclaysException;
import com.barclays.cardservice.repository.CardRepository;
import com.barclays.cardservice.repository.CustomerRepository;

/**
 * CardCustomerServiceImpl - Card customer service interface implementation
 * @author Anshika
 *
 */
@Service(value = "cardCustomerService")
@Transactional
public class CardCustomerServiceImpl implements CardCustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	CardRepository cardRepository;
	
	/**
	 * getCustomerDetails
	 * @param customerId
	 * @return Customer details along with card details
	 * @throws BarclaysException
	 */
	@Override
	public CustomerCardsDTO getCustomerDetails(Integer customerId) throws BarclaysException {
		Optional<Customer> opt = customerRepository.findById(customerId);
		if (opt.isEmpty())
			throw new BarclaysException("Customer not found");
		
		Customer customer = opt.get();
		
		List<Card> cards = cardRepository.findByCustomer_customerId(customerId);
		List<CardDTO> cardDTOs = new ArrayList<>();
		cards.forEach(card -> {
			CardDTO cardDTO = new CardDTO();
			cardDTO.setCardId(card.getCardId());
			cardDTO.setCardNumber(card.getCardNumber());
			cardDTO.setExpiryDate(card.getExpiryDate());
			cardDTOs.add(cardDTO);
		});
		
		CustomerCardsDTO customerDTO = convertToDTO(customer, cardDTOs);
		
		return customerDTO;
	}

	/**
	 * convertToDTO - Convert Customer entity to CustomerCardDTO
	 * @param customer - Customer entity
	 * @param cardDTOs - CardDTO List
	 * @return
	 */
	private CustomerCardsDTO convertToDTO(Customer customer, List<CardDTO> cardDTOs) {
		CustomerCardsDTO customerDTO = new CustomerCardsDTO();
		customerDTO.setId(customer.getCustomerId());
		customerDTO.setEmail(customer.getEmailid());
		customerDTO.setName(customer.getName());
		customerDTO.setDob(customer.getDateOfBirth());
		customerDTO.setCards(cardDTOs);
		return customerDTO;
	}

	/**
	 * addCustomer - Add new customer
	 * @param customerDTO - CustomerDTO object
	 * @return Id of newly created customer
	 * @throws BarclaysException
	 */
	@Override
	public Integer addCustomer(CustomerDTO customerDTO) throws BarclaysException {
		Customer customer = new Customer();
		customer.setCustomerId(customerDTO.getId());
		customer.setName(customerDTO.getName());
		customer.setEmailid(customerDTO.getEmail());
		customer.setDateOfBirth(customerDTO.getDob());
		
		Customer newCustomer = customerRepository.save(customer);
		
		return newCustomer.getCustomerId();
	}

	/**
	 * issueCardToExistingCustomer - Issue new card to customer
	 * @param customerId - customer id
	 * @param cardDTO - CardDTO object
	 * @throws BarclaysException
	 */
	@Override
	public void issueCardToExistingCustomer(Integer customerId, CardDTO cardDTO) throws BarclaysException {
		Optional<Customer> opt = customerRepository.findById(customerId);
		if (opt.isEmpty())
			throw new BarclaysException("Custome not found");
		
		Card card = new Card();
		card.setCardId(cardDTO.getCardId());
		card.setCardNumber(cardDTO.getCardNumber());
		card.setExpiryDate(cardDTO.getExpiryDate());
		card.setCustomer(opt.get());

		cardRepository.save(card);
	}

	/**
	 * deleteCustomer - Delete customer data along with their card(s) data
	 * @param customerId - customer id
	 * @throws BarclaysException
	 */
	@Override
	public void deleteCustomer(Integer customerId) throws BarclaysException {
		Optional<Customer> opt = customerRepository.findById(customerId);
		if (opt.isEmpty())
			throw new BarclaysException("Customer not found");
		
		Customer customer = opt.get();
		List<Card> cards = cardRepository.findByCustomer_customerId(customerId);
		cards.forEach(card -> cardRepository.delete(card));
		customerRepository.delete(customer);
	}

	/**
	 * deleteCardOfExistingCustomer - Delete only 
	 */
	@Override
	public void deleteCardOfExistingCustomer(Integer customerId, List<Integer> cardIdsToDelete) throws BarclaysException {
		Optional<Customer> opt = customerRepository.findById(customerId);
		if (opt.isEmpty())
			throw new BarclaysException("Customer not found");
		
		Customer customer = opt.get();
		
		for (Integer cardId: cardIdsToDelete) {
			Optional<Card> cardOpt = cardRepository.findById(cardId);
			
			if (cardOpt.isEmpty())
				throw new BarclaysException("Card not found");
			
			Card card = cardOpt.get();
			if (card.getCustomer().getCustomerId() != customer.getCustomerId()) 
				throw new BarclaysException("Card does not belong to customer");
			
			cardRepository.delete(card);
		}
	}
	
}
