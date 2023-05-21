package com.ssafy.mypage.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.service.BoardService;
import com.ssafy.hotplace.model.HotplaceDto;
import com.ssafy.hotplace.model.service.HotplaceService;
import com.ssafy.like.model.LikeDto;
import com.ssafy.mypage.model.service.MypageService;
import com.ssafy.plan.model.PlanDto;
import com.ssafy.plan.model.service.PlanService;
import com.ssafy.user.model.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/mypage")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class MypageController {
	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	@Value("${file.path.profilePath}")
	private String profilePath;
	
	private MypageService mypageService;
	private BoardService boardService;
	private HotplaceService hotplaceService;
	private PlanService planService;
	private UserService userService;
	
	@Autowired
	public MypageController(MypageService mypageService, BoardService boardService, HotplaceService hotplaceService, PlanService planService, UserService userService) {
		super();
		this.mypageService = mypageService;
		this.boardService = boardService;
		this.hotplaceService = hotplaceService;
		this.planService = planService;
		this.userService = userService;
	}
	
	@PostMapping("/profile")
	@ApiOperation(value = "사용자 프로필 사진을 업로드한다", response = Integer.class)
	@Transactional
	private ResponseEntity<?> uploadProfile(
			@RequestParam("userId") String userId,
			@RequestParam("image") MultipartFile file
	) {
		logger.debug("upload profile : {}", userId);
		if(!file.isEmpty()) {
			String saveFolder = profilePath;
			logger.debug("저장 폴더 : {}", saveFolder);
			File folder = new File(saveFolder);
			if (!folder.exists())
				folder.mkdirs();
			
			String originalFileName = file.getOriginalFilename();
			
			if (!originalFileName.isEmpty()) {
				String saveFileName = UUID.randomUUID().toString()
						+ originalFileName.substring(originalFileName.lastIndexOf('.'));
//				hotplaceDto.setImage(saveFileName);
				logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", file.getOriginalFilename(), saveFileName);
				
				try {
					file.transferTo(new File(folder, saveFileName));
					int result = userService.uploadProfile(userId, saveFileName);
					return new ResponseEntity<Integer>(result, HttpStatus.CREATED);
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
				}
			}
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		
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
