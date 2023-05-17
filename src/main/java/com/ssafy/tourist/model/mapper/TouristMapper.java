package com.ssafy.tourist.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.tourist.model.AttractionInfoDto;
import com.ssafy.tourist.model.GugunDto;
import com.ssafy.tourist.model.SidoDto;

@Mapper
public interface TouristMapper {
	/** sido(시도) 리스트 불러오는 메소드 */
    List<SidoDto> listSido() throws SQLException;
    
    List<GugunDto> listGugun(String sidoCode) throws SQLException;
    
	/** sido_code와 gugun_code로 관광지 리스트 불러오는 메소드 */
	List<AttractionInfoDto> listTourist(Map<String, String> param) throws SQLException;
//	    List<AttractionInfoDto> getTouristList(int sidoCode, int gugunCode, int contentTypeId) throws SQLException;
//	List<AttractionInfoDto> listTourist(int sidoCode, int gugunCode, int contentTypeId) throws SQLException;

	String getImageUrl(int id) throws SQLException;
}
