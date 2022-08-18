package com.barclays.cardservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.barclays.cardservice.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	
}
