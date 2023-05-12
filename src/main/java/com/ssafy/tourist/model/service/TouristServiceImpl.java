package com.ssafy.tourist.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.tourist.model.AttractionInfoDto;
import com.ssafy.tourist.model.SidoDto;
import com.ssafy.tourist.model.mapper.TouristMapper;

@Service
public class TouristServiceImpl implements TouristService {

	private TouristMapper touristMapper;
	
	public TouristServiceImpl(TouristMapper touristMapper) {
		super();
		this.touristMapper = touristMapper;
	}

	@Override
	public List<AttractionInfoDto> listTourist(Map<String, String> param) throws SQLException {
		return touristMapper.listTourist(param);
	}

	@Override
	public List<SidoDto> listSido() throws SQLException {
		return touristMapper.listSido();
	}
	
}
