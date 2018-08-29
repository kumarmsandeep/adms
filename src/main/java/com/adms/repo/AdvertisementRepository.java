package com.adms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adms.model.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

	
}
