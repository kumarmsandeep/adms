package com.adms.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Advertisement {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String url;

	private int cost;

	private int duration;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "advertisement")
	private List<PricingFactor> pricingFactors;

	public Advertisement() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getCostDetails() {
		String str = "";
		str += cost;
		if(pricingFactors != null && !pricingFactors.isEmpty()) {
			str += "</br>";
			for (PricingFactor pricingFactor : pricingFactors) {
				str += pricingFactor.getType() +" : ["+pricingFactor.getFromTime() + " - " + pricingFactor.getToTime()+"]</br>";				
			}
		}
		return str;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return name;
	}

	public List<PricingFactor> getPricingFactors() {
		return pricingFactors;
	}

	public void setPricingFactors(List<PricingFactor> pricingFactors) {
		this.pricingFactors = pricingFactors;
	}

}
