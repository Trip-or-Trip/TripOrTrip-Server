package com.ssafy.plan.model;

import java.sql.Date;
import java.sql.Timestamp;

public class PlanDto {
	private int id;
	private String title;
	private String description;
	private String createdAt;
	private String updatedAt;
	private String startDate;
	private String endDate;
	private String userId;
	private int hit;
	private PlaceDto places[];
	
	
	public PlaceDto[] getPlaces() {
		return places;
	}

	public void setPlaces(PlaceDto[] places) {
		this.places = places;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	@Override
	public String toString() {
		return "PlanDto [id=" + id + ", title=" + title + ", description=" + description + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
