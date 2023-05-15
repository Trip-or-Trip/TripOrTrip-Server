package com.ssafy.plan.model;

import java.math.BigDecimal;

public class PlaceDto {
	private int id;
	/** palnDto의 id */
	private int planId;
	/** 여행지 고유 id */
	private int placeId;
	private String name;
	private String address;
	/** 위도 */
	private BigDecimal lat;
	/** 경도 */
	private BigDecimal lng;
	private String imageUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "PlaceDto [id=" + id + ", planId=" + planId + ", name=" + name + ", address=" + address + ", lat=" + lat
				+ ", lng=" + lng + ", imageUrl=" + imageUrl + "]";
	}

}
