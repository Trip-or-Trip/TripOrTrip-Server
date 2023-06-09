package com.ssafy.comment.model;

public class CommentDto {
	private int id;
	private String content;
	private String  createdAt;
	private int boardId;
	private String userId;
	private String image;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
	@Override
	public String toString() {
		return "CommentDto [id=" + id + ", content=" + content + ", createdAt=" + createdAt + ", boardId=" + boardId
				+ ", userId=" + userId + ", image=" + image + "]";
	}
	
}
