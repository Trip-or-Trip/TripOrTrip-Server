package com.ssafy.notice.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.notice.model.NoticeDto;

@Mapper
public interface NoticeMapper {

	void writeArticle(NoticeDto noticeDto) throws SQLException;
	List<NoticeDto> listArticle(BoardParameterDto boardParameterDto) throws SQLException;
	int getTotalArticleCount(Map<String, Object> param) throws SQLException;
	NoticeDto getArticle(int articleNo) throws SQLException;
	void updateHit(int articleNo) throws SQLException;
	
	void modifyArticle(NoticeDto noticeDto) throws SQLException;
	void deleteArticle(int articleNO) throws SQLException;
	
}
