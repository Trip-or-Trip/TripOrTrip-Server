package com.ssafy.hotplace.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.hotplace.model.HotplaceDto;
import com.ssafy.util.PageNavigation;

public interface HotplaceService {
//	List<HotplaceDto> listHotplace(Map<String, String> map) throws Exception;
	List<HotplaceDto> listHotplace() throws SQLException;
//	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
//	int findLatestNum() throws Exception;
	int insertHotplace(HotplaceDto hotplaceDto) throws SQLException;
	HotplaceDto viewHotplace(String num) throws SQLException;
	
	String findOriginalImage(String num) throws SQLException;
	int updateHotplace(HotplaceDto hotplaceDto) throws SQLException;
	
	int deleteHotplace(String num) throws SQLException;
}
