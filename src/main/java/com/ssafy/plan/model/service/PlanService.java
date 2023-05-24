package com.ssafy.plan.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.plan.model.PlaceDto;
import com.ssafy.plan.model.PlanDto;
import com.ssafy.util.PageNavigation;

public interface PlanService {

	/** 여행 경로 추가 */
	public int insertPlan(PlanDto planDto) throws SQLException;

	/** 여행지 추가 */
	public int insertPlace(PlaceDto placeDto) throws SQLException;

	/** 여행 경로 삭제 */
	public int deletePlan(int id) throws SQLException;

	/** 여행 경로 리스트 출력 */
	public List<PlanDto> listPlan(BoardParameterDto boardParameterDto) throws SQLException;
	
	/** 인기 여행 계획 리스트 출력 */
	public List<PlanDto> listHotPlan() throws SQLException;
	
	/** 내가 작성한 여행 경로 리스트 출력 */
	public List<PlanDto> listMyPlan(String userId) throws SQLException;
	
	/** 글 번호에 맞는 여행 경로 출력 */
	public PlanDto selectPlanOne(int articleNo) throws SQLException;

	/** 여행 경로에 맞는 여행지 리스트 출력 */
	public List<PlaceDto> selectPlace(int planId) throws SQLException;

	/** 여행 경로 id 가져오기 */
	public int selectPlanId(String userId, String title) throws SQLException;
	
	/** 조회수 증가 */
	public int updateHit(int articleNo) throws SQLException;
	
	/** 최단 경로 여행지 리스트 출력 */
	public List<PlaceDto> selectFastDistancePlace(int planId) throws SQLException;
	
	/** planId에 해당하는 places 리스트 출력 */
	public List<PlaceDto> getPlanPlaces(int planId) throws SQLException;
	
	/** 여행 글 목록 네비게이션 */
	public PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
}
