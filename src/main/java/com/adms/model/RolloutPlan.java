package com.adms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RolloutPlan {

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fromDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date toDate;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "advertisement_id")
	private Advertisement advertisement;

	public RolloutPlan() {
		
	}

	public Long getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Advertisement getAdvertisement() {
		return advertisement;
	}

	public void setAdvertisement(Advertisement advertisement) {
		this.advertisement = advertisement;
	}
	
	public String getCustomerName() {
		if(this.customer != null) {
			return this.customer.getName();
		}
		return "--";
	}
	
	public String getAdvertisementName() {
		if(this.advertisement != null) {
			return this.advertisement.getName();
		}
		return "--";
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
