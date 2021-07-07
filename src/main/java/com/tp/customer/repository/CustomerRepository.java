package com.tp.customer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tp.customer.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
}
