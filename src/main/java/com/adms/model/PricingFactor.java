package com.adms.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
// @Table(uniqueConstraints=@UniqueConstraint(columnNames={"type","fk_advertisement"}))
public class PricingFactor {

	@Id
	@GeneratedValue
	private Long id;

	private String type;

	private int fromTime;

	private int toTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "advertisement_id", nullable = false)
	private Advertisement advertisement;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getFromTimeString() {
		int minutes = fromTime % 100;
		int hours = (fromTime /100);
		return String.format("%02d", hours) + " : " + String.format("%02d", minutes);
	}

	public int getFromTime() {
		return fromTime;
	}

	public void setFromTime(int fromTime) {
		this.fromTime = fromTime;
	}
	
	public String getToTimeString() {
		int minutes = toTime % 100;
		int hours = (toTime /100);
		return String.format("%02d", hours) + " : " + String.format("%02d", minutes);
	}

	public int getToTime() {
		return toTime;
	}

	public void setToTime(int toTime) {
		this.toTime = toTime;
	}

	public Advertisement getAdvertisement() {
		return advertisement;
	}

	public void setAdvertisement(Advertisement advertisement) {
		this.advertisement = advertisement;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
