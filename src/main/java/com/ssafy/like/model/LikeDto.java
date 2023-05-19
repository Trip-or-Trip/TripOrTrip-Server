package com.ssafy.like.model;

public class LikeDto {
	private int id;
	private int hotplaceId;
	private String userId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHotplaceId() {
		return hotplaceId;
	}
	public void setHotplaceId(int hotplaceId) {
		this.hotplaceId = hotplaceId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "LikeDto [id=" + id + ", hotplaceId=" + hotplaceId + ", userId=" + userId + "]";
	}
	
	
}
