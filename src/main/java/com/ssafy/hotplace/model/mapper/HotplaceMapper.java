package com.ssafy.hotplace.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.hotplace.model.HotplaceDto;
import com.ssafy.like.model.LikeDto;

@Mapper
public interface HotplaceMapper {
//	List<HotplaceDto> listHotplace(Map<String, Object> param) throws Exception;
	List<HotplaceDto> listHotplace() throws SQLException;
//	int getTotalHotplaceCount(Map<String, Object> param) throws Exception;
//	int findLatestNum() throws Exception;
	int insertHotplace(HotplaceDto hotplaceDto) throws SQLException;
	HotplaceDto viewHotplace(String num) throws SQLException;
	
	String findOriginalImage(String num) throws SQLException;
	int updateHotplace(HotplaceDto hotplaceDto) throws SQLException;

	int deleteHotplace(String num) throws SQLException;
	
	void plusLikeCnt(String num) throws SQLException;
	void minusLikeCnt(String num) throws SQLException;
	
	int insertLike(Map<String, String> map) throws Exception;
	int deleteLike(Map<String, String> map) throws Exception;
	
	List<LikeDto> listLike(String num) throws SQLException;
}
