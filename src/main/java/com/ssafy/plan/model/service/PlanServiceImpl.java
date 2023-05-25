package com.ssafy.plan.model.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.plan.model.PlaceDto;
import com.ssafy.plan.model.PlanDto;
import com.ssafy.plan.model.mapper.PlanMapper;
import com.ssafy.util.PageNavigation;
import com.ssafy.util.SizeConstant;

@Service
public class PlanServiceImpl implements PlanService {

	private static int[] pick, resPick;
	private static boolean[] visited;
	private static long[][] D;
	private static long minSum;

	private PlanMapper planMapper;

	
	public PlanServiceImpl(PlanMapper planMapper) {
		super();
		this.planMapper = planMapper;
	}

	@Override
	public int insertPlan(PlanDto planDto) throws SQLException {
		return planMapper.insertPlan(planDto);
	}

	@Override
	public int insertPlace(PlaceDto placeDto) throws SQLException {
		return planMapper.insertPlace(placeDto);
	}
	
	@Override
	public int updatePlan(PlanDto planDto) throws SQLException {
		return planMapper.updatePlan(planDto);
	}

	@Override
	public int updatePlace(PlaceDto placeDto) throws SQLException {
		return planMapper.updatePlace(placeDto);
	}

	@Override
	public int deletePlan(int id) throws SQLException {
		return planMapper.deletePlan(id);
	}
	
	@Override
	public int deletePlace(int planId) throws SQLException {
		return planMapper.deletePlace(planId);
	}

	@Override
	public List<PlanDto> listPlan(BoardParameterDto boardParameterDto) throws SQLException {
		int start = boardParameterDto.getPg() == 0 ? 0 : (boardParameterDto.getPg() - 1) * boardParameterDto.getSpp();
		boardParameterDto.setStart(start);
		return planMapper.listPlan(boardParameterDto);
	}
	
	@Override
	public List<PlanDto> listHotPlan() throws SQLException {
		return planMapper.listHotPlan();
	}
	
	@Override
	public List<PlaceDto> listPlaces() throws SQLException {
		return planMapper.listPlaces();
	}
	
	@Override
	public List<PlanDto> listMyPlan(String userId) throws SQLException {
		return planMapper.listMyPlan(userId);
	}

	@Override
	public PlanDto selectPlanOne(int articleNo) throws SQLException {
		return planMapper.selectPlanOne(articleNo);
	}

	@Override
	public List<PlaceDto> selectPlace(int planId) throws SQLException {
		return planMapper.selectPlace(planId);
	}

	@Override
	public int selectPlanId(String userId) throws SQLException {
		return planMapper.selectPlanId(userId);
	}

	@Override
	public int updateHit(int articleNo) throws SQLException {
		return planMapper.updateHit(articleNo);
	}

	/** 알고리즘 적용 - 완전탐색 (순열) */
	public List<PlaceDto> selectFastDistancePlace(int planId) throws SQLException {
		List<PlaceDto> list = selectPlace(planId);
		List<PlaceDto> resultList = new ArrayList<>();
		int cnt = planMapper.getTotalPlaceCount(planId);
		// 최단 거리 알고리즘 사용 : 완전 탐색
		D = new long[cnt][cnt];

		// 서로 간의 거리 구하기
		for (int i = 0; i < cnt; i++) {
			for (int j = i + 1; j < cnt; j++) {
				PlaceDto start = list.get(i);
				PlaceDto end = list.get(j);

				long startLat = (long) (start.getLat().doubleValue() * Math.pow(10, 13));
				long endLat = (long) (end.getLat().doubleValue() * Math.pow(10, 13));
				long startLng = (long) (start.getLng().doubleValue() * Math.pow(10, 13));
				long endLng = (long) (end.getLng().doubleValue() * Math.pow(10, 13));

				D[i][j] = Math.abs(startLat - endLat) + Math.abs(startLng - endLng);
				D[j][i] = D[i][j];
			}
		}

		// 초기화
		visited = new boolean[cnt];
		pick = new int[cnt];
		resPick = new int[cnt];
		minSum = Long.MAX_VALUE;
		recur(0, cnt);

		for (int i = 0; i < cnt; i++) {
			int idx = resPick[i];
			resultList.add(list.get(idx));
		}
		return resultList;
	}

	public static void recur(int cnt, int n) {
		if (cnt == n) {
			fastSequence(n);
			return;
		}

		for (int i = 0; i < n; i++) {
			if (visited[i])
				continue;
			visited[i] = true;
			pick[cnt] = i;
			recur(cnt + 1, n);
			visited[i] = false;
		}
	}

	public static void fastSequence(int n) {
		long sum = 0;
		for (int i = 0; i < n - 1; i++) {
			sum += D[pick[i]][pick[i + 1]];
		}

		if (sum < minSum) {
			minSum = sum;

			for (int i = 0; i < n; i++) {
				resPick[i] = pick[i];
			}
		}
	}

	@Override
	public PageNavigation makePageNavigation(Map<String, String> map) throws Exception {
		PageNavigation pageNavigation = new PageNavigation();

		int naviSize = SizeConstant.NAVIGATION_SIZE;
		int sizePerPage = SizeConstant.LIST_SIZE;
		int currentPage = Integer.parseInt(map.get("pgno"));

		pageNavigation.setCurrentPage(currentPage);
		pageNavigation.setNaviSize(naviSize);
		Map<String, Object> param = new HashMap<String, Object>();
		String key = map.get("key");
//		if("userid".equals(key))
//			key = "user_id";
		param.put("key", key.isEmpty() ? "" : key);
		param.put("word", map.get("word").isEmpty() ? "" : map.get("word"));
		int totalCount = planMapper.getTotalArticleCount(param);
		pageNavigation.setTotalCount(totalCount);
		int totalPageCount = (totalCount - 1) / sizePerPage + 1;
		pageNavigation.setTotalPageCount(totalPageCount);
		boolean startRange = currentPage <= naviSize;
		pageNavigation.setStartRange(startRange);
		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < currentPage;
		pageNavigation.setEndRange(endRange);
		pageNavigation.makeNavigator();

		return pageNavigation;
	}

	/** KMP 알고리즘 */
	public static int KMP(char[] text, char[] pattern) {
		int tLength = text.length, pLength = pattern.length;

		// 부분일치테이블 만들기
		int[] pi = new int[pLength];
		// i:접미사 포인터(i=1부터 시작), j:접두사 포인터
		for (int i = 1, j = 0; i < pLength; i++) {
			while (j > 0 && pattern[i] != pattern[j])
				j = pi[j - 1];

			if (pattern[i] == pattern[j])
				pi[i] = ++j;
			else
				pi[i] = 0;
		}

		int cnt = 0;

		// i : 텍스트 포인터 , j: 패턴 포인터
		for (int i = 0, j = 0; i < tLength; ++i) {

			while (j > 0 && text[i] != pattern[j])
				j = pi[j - 1];

			if (text[i] == pattern[j]) { // 두 글자 일치
				if (j == pLength - 1) { // j가 패턴의 마지막 인덱스라면
					cnt++; // 카운트 증가
					j = pi[j];
				} else {
					j++;
				}
			}
		}

		// cnt 값을 반환
		return cnt;
	}

	@Override
	public List<PlaceDto> getPlanPlaces(int planId) throws SQLException {
		return planMapper.getPlanPlaces(planId);
	}



}
