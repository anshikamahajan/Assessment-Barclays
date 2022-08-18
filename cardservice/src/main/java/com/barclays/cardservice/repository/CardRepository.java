package com.barclays.cardservice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.barclays.cardservice.entity.Card;

public interface CardRepository extends CrudRepository<Card, Integer> {
	List<Card> findByCustomer_customerId(Integer id);
}
