package com.adms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.adms.model.Customer;
import com.adms.repo.AdvertisementRepository;
import com.adms.repo.CustomerRepository;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

//	@Bean
//	public CommandLineRunner loadData(CustomerRepository repository, AdvertisementRepository advertisementRepository) {
//		return (args) -> {
//			
//			// save a couple of customers
//			repository.save(new Customer("Jack", "Bauer", "",""));
//			repository.save(new Customer("Chloe", "O'Brian", "",""));
//			repository.save(new Customer("Kim", "Bauer","",""));
//			repository.save(new Customer("David", "Palmer","",""));
//			repository.save(new Customer("Michelle", "Dessler","",""));			
//		};
//	}

}
