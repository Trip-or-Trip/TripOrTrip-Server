package com.ssafy.mypage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.board.model.service.BoardService;
import com.ssafy.hotplace.model.HotplaceDto;
import com.ssafy.hotplace.model.service.HotplaceService;
import com.ssafy.mypage.model.service.MypageService;
import com.ssafy.plan.model.PlanDto;
import com.ssafy.plan.model.service.PlanService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/mypage")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class MypageController {
	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	private MypageService mypageService;
	private BoardService boardService;
	private HotplaceService hotplaceService;
	private PlanService planService;
	
	@Autowired
	public MypageController(MypageService mypageService, BoardService boardService, HotplaceService hotplaceService, PlanService planService) {
		super();
		this.mypageService = mypageService;
		this.boardService = boardService;
		this.hotplaceService = hotplaceService;
		this.planService = planService;
	}
	
	@GetMapping("/article/board")
	@ApiOperation(value = "내가 쓴 게시판 목록을 반환한다.", response = List.class)
	private ResponseEntity<?> listBoard(@ApiParam(value = "내가 쓴 게시글을 얻기위해 현재 로그인한 user id 정보.", required = true) String userId) {
		logger.debug("Mypage("+userId+") : listBoard call");
		try {
			List<BoardDto> list = boardService.listMyArticle(userId);
			return new ResponseEntity<List<BoardDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/article/hotplace")
	@ApiOperation(value = "내가 쓴 게시판 목록을 반환한다.", response = List.class)
	private ResponseEntity<?> listHotplace(@ApiParam(value = "내가 쓴 핫플레이스를 얻기위해 현재 로그인한 user id 정보.", required = true) String userId) {
		logger.debug("Mypage("+userId+") : listHotplace call");
		try {
			List<HotplaceDto> list = hotplaceService.listMyHotplace(userId);
			return new ResponseEntity<List<HotplaceDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/article/plan")
	@ApiOperation(value = "내가 쓴 여행계획 목록을 반환한다.", response = List.class)
	private ResponseEntity<?> listPlan(@ApiParam(value = "내가 쓴 여행계획을 얻기위해 현재 로그인한 user id 정보.", required = true) String userId) {
		logger.debug("Mypage("+userId+") : listPlan call");
		try {
			List<PlanDto> list = planService.listMyPlan(userId);
			return new ResponseEntity<List<PlanDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	//TODO: /like 메서드 구현
}
