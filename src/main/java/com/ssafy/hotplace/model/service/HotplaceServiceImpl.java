package com.ssafy.hotplace.model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.hotplace.model.HotplaceDto;
import com.ssafy.hotplace.model.mapper.HotplaceMapper;
import com.ssafy.util.PageNavigation;
import com.ssafy.util.SizeConstant;

@Service
public class HotplaceServiceImpl implements HotplaceService {
	
	@Autowired
	private HotplaceMapper hotplaceMapper;
	
	public HotplaceServiceImpl(HotplaceMapper hotplaceMapper) {
		super();
		this.hotplaceMapper = hotplaceMapper;
	}

//	@Override
//	public List<HotplaceDto> listHotplace(Map<String, String> map) throws Exception {
//		Map<String, Object> param = new HashMap<String, Object>();
//		int page = Integer.parseInt(map.get("page"));
//		int start = page * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
//		param.put("start", start);
//		param.put("listsize", SizeConstant.LIST_SIZE);
//		return hotplaceMapper.listHotplace(param);
//	}
	
	@Override
	public List<HotplaceDto> listHotplace() throws SQLException {
		return hotplaceMapper.listHotplace();
	}

//	@Override
//	public PageNavigation makePageNavigation(Map<String, String> map) throws Exception {
//		PageNavigation pageNavigation = new PageNavigation();
//
//		int naviSize = SizeConstant.NAVIGATION_SIZE;
//		int sizePerPage = SizeConstant.LIST_SIZE;
//		int currentPage = Integer.parseInt(map.get("page"));
//
//		pageNavigation.setCurrentPage(currentPage);
//		pageNavigation.setNaviSize(naviSize);
//		Map<String, Object> param = new HashMap<String, Object>();
//		int totalCount = hotplaceMapper.getTotalHotplaceCount(param);
//		pageNavigation.setTotalCount(totalCount);
//		int totalPageCount = (totalCount - 1) / sizePerPage + 1;
//		pageNavigation.setTotalPageCount(totalPageCount);
//		boolean startRange = currentPage <= naviSize;
//		pageNavigation.setStartRange(startRange);
//		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < currentPage;
//		pageNavigation.setEndRange(endRange);
//		pageNavigation.makeNavigator();
//
//		return pageNavigation;
//	}

//	@Override
//	public int findLatestNum() throws Exception {
//		return hotplaceMapper.findLatestNum();
//	}

	@Override
	public int insertHotplace(HotplaceDto hotplaceDto) throws SQLException {
		return hotplaceMapper.insertHotplace(hotplaceDto);
	}

	@Override
	public HotplaceDto viewHotplace(String num) throws SQLException {
		return hotplaceMapper.viewHotplace(num);
	}

	@Override
	public String findOriginalImage(String num) throws SQLException {
		return hotplaceMapper.findOriginalImage(num);
	}

	@Override
	public int updateHotplace(HotplaceDto hotplaceDto) throws SQLException {
		return hotplaceMapper.updateHotplace(hotplaceDto);
	}

	@Override
	public int deleteHotplace(String num) throws SQLException {
		return hotplaceMapper.deleteHotplace(num);
	}
	
	
}
