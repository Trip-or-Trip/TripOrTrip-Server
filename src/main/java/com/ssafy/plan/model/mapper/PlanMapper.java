package com.ssafy.plan.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.plan.model.PlaceDto;
import com.ssafy.plan.model.PlanDto;

@Mapper
public interface PlanMapper {

	/** 여행 경로 추가 */
	public int insertPlan(PlanDto planDto) throws SQLException;

	/** 여행지 추가 */
	public int insertPlace(PlaceDto placeDto) throws SQLException;

	/** 여행 경로 수정 */
	public int updatePlan(PlanDto planDto) throws SQLException;
	
	/** 여행지 수정 */
	public int updatePlace(PlaceDto placeDto) throws SQLException;

	/** 여행 경로 삭제 */
	public int deletePlan(int id) throws SQLException;
	
	/** 여행지 삭제 */
	public int deletePlace(int planId) throws SQLException;

	/** 여행 경로 리스트 출력 */
	public List<PlanDto> listPlan(BoardParameterDto boardParameterDto) throws SQLException;

	/** 인기 여행 경로 리스트 출력 */
	public List<PlanDto> listHotPlan() throws SQLException;
	
	/** 인기 여행 장소 리스트 출력 */
	public List<PlaceDto> listPlaces() throws SQLException;

	/** 내가 작성한 여행 경로 리스트 출력 */
	public List<PlanDto> listMyPlan(String userId) throws SQLException;
	
	/** 글 번호에 맞는 여행 경로 출력 */
	public PlanDto selectPlanOne(int articleNo) throws SQLException;

	/** 여행 경로에 맞는 여행지 리스트 출력 */
	public List<PlaceDto> selectPlace(int planId) throws SQLException;
	
	/** planId에 해당하는 모든 places return */
	public List<PlaceDto> getPlanPlaces(int planId) throws SQLException;
	
	/** 여행 경로 id 가져오기 */
	public int selectPlanId(String userId) throws SQLException;
	
	/** 조회수 증가 */
	public int updateHit(int articleNo) throws SQLException;
	
	public int getTotalArticleCount(Map<String, Object> param) throws SQLException;
	
	public int getTotalPlaceCount(int planId) throws SQLException;
}
