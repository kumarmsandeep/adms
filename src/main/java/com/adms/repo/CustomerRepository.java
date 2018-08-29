package com.adms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adms.model.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	
}
