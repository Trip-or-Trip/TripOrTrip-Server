package com.ssafy.mypage.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.service.BoardService;
import com.ssafy.hotplace.model.HotplaceDto;
import com.ssafy.hotplace.model.service.HotplaceService;
import com.ssafy.like.model.LikeDto;
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
	
	@PostMapping("/board")
	@ApiOperation(value = "내가 쓴 게시판 목록을 반환한다.", response = List.class)
	private ResponseEntity<?> listBoard(@RequestBody @ApiParam(value = "내가 쓴 게시글을 얻기위해 현재 로그인한 user id 정보.", required = true) String userId) {
		logger.debug("Mypage("+userId+") : listBoard call");
		userId = userId.substring(1, userId.length()-1);
		try {
			List<BoardDto> list = boardService.listMyArticle(userId);
			return new ResponseEntity<List<BoardDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping("/hotplace")
	@ApiOperation(value = "내가 쓴 게시판 목록을 반환한다.", response = List.class)
	private ResponseEntity<?> listHotplace(@RequestBody @ApiParam(value = "내가 쓴 핫플레이스를 얻기위해 현재 로그인한 user id 정보.", required = true) String userId) {
		logger.debug("Mypage("+userId+") : listHotplace call");
		userId = userId.substring(1, userId.length()-1);
		try {
			List<HotplaceDto> list = hotplaceService.listMyHotplace(userId);
			return new ResponseEntity<List<HotplaceDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping("/plan")
	@ApiOperation(value = "내가 쓴 여행계획 목록을 반환한다.", response = List.class)
	private ResponseEntity<?> listPlan(@RequestBody @ApiParam(value = "내가 쓴 여행계획을 얻기위해 현재 로그인한 user id 정보.", required = true) String userId) {
		logger.debug("Mypage("+userId+") : listPlan call");
		userId = userId.substring(1, userId.length()-1);
		try {
			List<PlanDto> list = planService.listMyPlan(userId);
			return new ResponseEntity<List<PlanDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	//TODO: /like 메서드 구현
	@PostMapping("/like")
	@ApiOperation(value = "내가 좋아요 누른 핫플 목록을 반환한다.", response = List.class)
	private ResponseEntity<?> listLike(@RequestBody @ApiParam(value = "내가 좋아요 한 핫플 리스트를 얻기위해 현재 로그인한 user id 정보.", required = true) String userId) {
		logger.debug("Mypage("+userId+") : listLike call");
		userId = userId.substring(1, userId.length()-1);
		try {
			List<LikeDto> list = hotplaceService.listLike(userId);
			List<HotplaceDto> likedHotplace = new ArrayList<>();
			for(LikeDto id : list) {
				HotplaceDto item = hotplaceService.viewHotplace(""+id.getHotplaceId());
				likedHotplace.add(item);
			}
			return new ResponseEntity<List<HotplaceDto>>(likedHotplace, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
}
