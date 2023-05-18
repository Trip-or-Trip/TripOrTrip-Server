package com.ssafy.hotplace.model;

public class HotplaceDto {
	private String userId;
	private int num;
	private String image;
	private String title;
	private String joinDate;
	private String desc;
	private String tag1;
	private String tag2;
	private double latitude;
	private double longitude;
	private String mapUrl;
	private boolean like = false;
	
	public boolean getLike() {
		return like;
	}
	public void setLike(boolean like) {
		this.like = like;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTag1() {
		return tag1;
	}
	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}
	public String getTag2() {
		return tag2;
	}
	public void setTag2(String tag2) {
		this.tag2 = tag2;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getMapUrl() {
		return mapUrl;
	}
	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[userId=");
		builder.append(userId);
		builder.append(", num=");
		builder.append(num);
		builder.append(", image=");
		builder.append(image);
		builder.append(", title=");
		builder.append(title);
		builder.append(", joinDate=");
		builder.append(joinDate);
		builder.append(", desc=");
		builder.append(desc);
		builder.append(", tag1=");
		builder.append(tag1);
		builder.append(", tag2=");
		builder.append(tag2);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", mapUrl=");
		builder.append(mapUrl);
		builder.append("]");
		return builder.toString();
	}
}
