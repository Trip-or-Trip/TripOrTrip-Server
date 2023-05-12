package com.ssafy.notice.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.notice.model.NoticeDto;
import com.ssafy.util.PageNavigation;

public interface NoticeService {

	void writeArticle(NoticeDto noticeDto) throws Exception;
	List<NoticeDto> listArticle(BoardParameterDto boardParameterDto) throws Exception;
	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
	NoticeDto getArticle(int articleNo) throws Exception;
	void updateHit(int articleNo) throws Exception;
	
	void modifyArticle(NoticeDto noticeDto) throws Exception;
	void deleteArticle(int articleNo) throws Exception;
	
}