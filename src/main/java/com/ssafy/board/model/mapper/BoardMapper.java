package com.ssafy.board.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.comment.model.CommentDto;

@Mapper
public interface BoardMapper {

	void writeArticle(BoardDto boardDto) throws SQLException;
	void writeComment(CommentDto commentDto) throws SQLException;
	List<BoardDto> listArticle(BoardParameterDto boardParameterDto) throws SQLException;
	List<BoardDto> listHotArticle() throws SQLException;
//	List<BoardDto> listArticle(Map<String, Object> param) throws SQLException;
	List<CommentDto> getComment(int articleNo) throws SQLException;
	List<CommentDto> getCommentList(String userId) throws SQLException;
	List<BoardDto> listMyArticle(String userId) throws SQLException;
	int getTotalArticleCount(Map<String, Object> param) throws SQLException;
	BoardDto getArticle(int articleNo) throws SQLException;
	void updateHit(int articleNo) throws SQLException;
	
	void modifyArticle(BoardDto boardDto) throws SQLException;
	void deleteArticle(int articleNO) throws SQLException;
	void deleteComment(int articleNO) throws SQLException;
}
