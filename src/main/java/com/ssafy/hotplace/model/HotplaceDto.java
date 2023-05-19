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
	private int likeCnt;
	
	public HotplaceDto() {}
	
	public HotplaceDto(String userId, String title, String joinDate, String desc, String tag1,
			String tag2, double latitude, double longitude, String mapUrl) {
		super();
		this.userId = userId;
		this.title = title;
		this.joinDate = joinDate;
		this.desc = desc;
		this.tag1 = tag1;
		this.tag2 = tag2;
		this.latitude = latitude;
		this.longitude = longitude;
		this.mapUrl = mapUrl;
	}
	
	
	public int getLikeCnt() {
		return likeCnt;
	}


	public void setLikeCnt(int likeCnt) {
		this.likeCnt = likeCnt;
	}


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
	public String gettag1() {
		return tag1;
	}
	public void settag1(String tag1) {
		this.tag1 = tag1;
	}
	public String gettag2() {
		return tag2;
	}
	public void settag2(String tag2) {
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
