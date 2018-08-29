package com.adms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adms.model.Customer;
import com.adms.model.RolloutPlan;

public interface RolloutPlanRepository extends JpaRepository<RolloutPlan, Long> {

	List<RolloutPlan> findByCustomer(Customer customer);

}
