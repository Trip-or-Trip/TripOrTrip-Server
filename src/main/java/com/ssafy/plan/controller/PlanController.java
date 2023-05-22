package com.ssafy.plan.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.plan.model.PlaceDto;
import com.ssafy.plan.model.PlanDto;
import com.ssafy.plan.model.service.PlanService;
import com.ssafy.user.model.UserDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/plan")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class PlanController {
	
	private final Logger logger = LoggerFactory.getLogger(PlanController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	private PlanService planService;

	@Autowired
	public PlanController(PlanService planService) {
		super();
		this.planService = planService;
	}
	
	@ApiOperation(value = "여행계획 목록", notes = "모든 여행계획 정보를 반환한다.", response = List.class)
	@GetMapping("/list")
	private ResponseEntity<?> listPlan(@ApiParam(value = "여행계획을 얻기위한 부가정보.", required = true)  BoardParameterDto boardParameterDto) {
		logger.info("PlanController :: listPlan - 호출 " );
		try {			
			List<PlanDto> list = planService.listPlan(boardParameterDto);
			return new ResponseEntity<List<PlanDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "여행계획 목록", notes = "모든 여행계획 정보를 반환한다.", response = List.class)
	@PostMapping("/list")
	private ResponseEntity<?> listKeywordPlan(@ApiParam(value = "여행계획을 얻기위한 부가정보.", required = true) @RequestBody BoardParameterDto boardParameterDto) {
		logger.info("PlanController :: listPlan Keyword - 호출 " );
		try {			
			List<PlanDto> list = planService.listPlan(boardParameterDto);
			return new ResponseEntity<List<PlanDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	/**
	 *  여행계획 상세 조회 메서드
	 * @param articleNo : 조회할 게시글 번호
	 * @param map : pgno, key, word 보관하는 map > Vue에서 처리 가능하면 생략가능
	 * @return Map<String, Object> : plan, place, fastplace 담고 있다.
	 */
	@ApiOperation(value = "여행계획 상세보기", notes = "글번호에 해당하는 여행계획 정보(Map)를 반환한다.", response = HashMap.class)
	@PostMapping("/{articleno}")
	private ResponseEntity<?> getPlan(@RequestBody @PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleNo, BoardParameterDto boardParameter) {
		try {
			logger.info("PlanController:: getPlan - 호출 : " + articleNo);
			Map<String, Object> result = new HashMap<>(); // PlanDto, PlaceDto, 추천경로 담는 HashMap
			
			PlanDto planDto = planService.selectPlanOne(articleNo);
			result.put("article", planDto);
			
			List<PlaceDto> list = planService.selectPlace(articleNo);
			result.put("places", list);
			
			List<PlaceDto> fastDistanceList = planService.selectFastDistancePlace(articleNo);
			result.put("fastPlaces", fastDistanceList);
			
			planService.updateHit(articleNo);
			
			// 기존에 Model로 받았었었는데 이제 Map으로 바꿨으니 Vue 에서도 Map으로 받아 처리 해줘야 함
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK); 
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	/** form 에서 여행 계획을 저장한다고 submit을 할 때 호출 됨 */
	@ApiOperation(value = "여행 계획 작성", notes = "새로운 여행계획을 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping("/write")
	private ResponseEntity<?> writePlan(@RequestBody @ApiParam(value = "여행계획 정보.", required = true) PlanDto planDto ){
		logger.info("PlanController:: writePlan - 호출 ");
		if(planDto.getPlaces().length == 0 ) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
		try {
			// 1. 여행지 계획 담기
			planService.insertPlan(planDto);
			planDto.setUserId("ssafy");
			// 2. 여행지 정보 담기			
			// 여행지 계획의 plan_id 가져오기
			
			int planId = planService.selectPlanId(planDto.getUserId(), planDto.getTitle());
			for (PlaceDto place : planDto.getPlaces()) {
				place.setPlanId(planId);
				System.out.println(place.toString());
				planService.insertPlace(place);
			}
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
		
	}
	
	@ApiOperation(value="여행 계획 수정", notes = "여행계획 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping("/modify")
	private ResponseEntity<String> modifyPlan(@RequestBody @ApiParam(value = "수정할 여행계획 정보.", required = true) PlanDto planDto) {
		logger.debug("PlanController: modifyPlan - 호출");
		try {
//			planService.modifyPlan(planDto);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "여행계획 삭제", notes = "글번호에 해당하는 여행계획을 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("/{articleno}")
	private ResponseEntity<String> deleteArticle(@PathVariable("articleno") @ApiParam(value = "살제할 글의 글번호.", required = true) int articleNo) {
		try {
			planService.deletePlan(articleNo);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e1) {
			return new ResponseEntity<String>(FAIL, HttpStatus.OK);
		}
	}
	
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
