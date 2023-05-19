package com.ssafy.hotplace.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssafy.hotplace.model.HotplaceDto;
import com.ssafy.hotplace.model.service.HotplaceService;
import com.ssafy.like.model.LikeDto;
import com.ssafy.user.model.UserDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping("/hotplace")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 50
)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
@Api("핫플레이스 컨트롤러  API V1")
public class HotplaceController {
	private final Logger logger = LoggerFactory.getLogger(HotplaceController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Value("${file.path.hotplacePath}")
	private String hotplacePath;
	
	private HotplaceService hotplaceService;

	public HotplaceController(HotplaceService hotplaceService) {
		super();
		this.hotplaceService = hotplaceService;
	}

	@ApiOperation(value = "핫플레이스 목록", notes = "모든 게시글의 정보를 반환한다.", response = Object.class)
	@PostMapping("/list")
	public ResponseEntity<?> list(@RequestBody @ApiParam(value = "핫플레이스 목록 조회 시 해당 사용자 아이디가 누른 좋아요를 표시하기 위한 아이디 입력.", required = true)  String userId) throws SQLException {
		logger.debug("hotplace list:" + userId);
		try {
			List<HotplaceDto> list = hotplaceService.listHotplace();
			List<LikeDto> listLike = hotplaceService.listLike(userId); // 해당 사용자가 좋아요 누른 list 반환(listLike에는 hotplace 번호가 담긴다)
			
			if(listLike.size() == 0 ) return new ResponseEntity<List<HotplaceDto>>(list, HttpStatus.OK);
			
			for(HotplaceDto hotplace : list) { // 모든 목록(DB)에 대해
				for(LikeDto likes : listLike) { // 사용자의 모든 좋아요 목록(DB)을 보면서 
					if(hotplace.getNum() == likes.getHotplaceId()) { // 같다면(좋아요를 누른 글이라면)
						hotplace.setLike(true); // 해당 게시글 좋아요 처리(Dto)
						break; // 탈출
					}
				}
			}
			return new ResponseEntity<List<HotplaceDto>>(list, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(FAIL, HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "핫플레이스 등록", notes = "핫플레이스를 등록한다.", response = Integer.class)
	@PostMapping("")
	@Transactional
	public ResponseEntity<?> insert(@RequestBody HotplaceDto hotplaceDto, @RequestParam("hotplace-image") MultipartFile file, HttpSession session) {
		UserDto userDto = (UserDto) session.getAttribute("userinfo");
		hotplaceDto.setUserId(userDto.getId());
		
		logger.debug("write hotplaceDto : {}", hotplaceDto);
		logger.debug("MultipartFile.isEmpty : {}", file.isEmpty());
		
		if(!file.isEmpty()) {
			String saveFolder = hotplacePath;
			logger.debug("저장 폴더 : {}", saveFolder);
			File folder = new File(saveFolder);
			if (!folder.exists())
				folder.mkdirs();
			
			String originalFileName = file.getOriginalFilename();
			if (!originalFileName.isEmpty()) {
				String saveFileName = UUID.randomUUID().toString()
						+ originalFileName.substring(originalFileName.lastIndexOf('.'));
				hotplaceDto.setImage(saveFileName);
				logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", file.getOriginalFilename(), saveFileName);
				
				try {
					file.transferTo(new File(folder, saveFileName));
					int result = hotplaceService.insertHotplace(hotplaceDto);
					return new ResponseEntity<Integer>(result, HttpStatus.CREATED);
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
				}
			}
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "핫플레이스 조회", notes = "글 번호에 해당하는 핫플레이스 정보를 반환한다.", response = HotplaceDto.class)
	@GetMapping("/{num}")
	public ResponseEntity<?> view(@PathVariable("num") String num) throws SQLException {
		HotplaceDto hotplaceDto = hotplaceService.viewHotplace(num);
		return new ResponseEntity<HotplaceDto>(hotplaceDto, HttpStatus.OK);
	}
	
	@ApiOperation(value = "핫플레이스 수정", notes = "핫플레이스를 수정한다.", response = Integer.class)
	@PutMapping("/{num}")
	@Transactional
	public ResponseEntity<?> update(HotplaceDto hotplaceDto, @RequestParam("hotplace-update-image") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) throws SQLException, IllegalStateException, IOException {
		UserDto userDto = (UserDto) session.getAttribute("userinfo");
		hotplaceDto.setUserId(userDto.getId());
		
		logger.debug("write hotplaceDto : {}", hotplaceDto);
		logger.debug("MultipartFile.isEmpty : {}", file.isEmpty());
		
		if(!file.isEmpty()) {
			String saveFolder = hotplacePath;
			logger.debug("저장 폴더 : {}", saveFolder);
			File folder = new File(saveFolder);
			if (!folder.exists())
				folder.mkdirs();
			
			String originalFileName = file.getOriginalFilename();
			if (!originalFileName.isEmpty()) {
				String saveFileName = UUID.randomUUID().toString()
						+ originalFileName.substring(originalFileName.lastIndexOf('.'));
				hotplaceDto.setImage(saveFileName);
				logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", file.getOriginalFilename(), saveFileName);
				
				try {
					file.transferTo(new File(folder, saveFileName));
					int result = hotplaceService.updateHotplace(hotplaceDto);
					return new ResponseEntity<Integer>(result, HttpStatus.CREATED);
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
				}
			}
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "핫플레이스 좋아요기능", notes = "글 번호에 해당하는 핫플레이스를 좋아요한다. \n 이미 좋아요 등록이 되어있다면 해제한다.", response = String.class)
	@PutMapping("")
	public ResponseEntity<String> like(/* userId랑 hotplaceId(num)을 받아야해 Map<String, String> map*/ String userId, String num){
		Map<String, String> map = new HashMap();
		map.put("num", num);
		map.put("userId", userId);
		
		System.out.println(map.toString());
		try {
			if(hotplaceService.deleteLike(map) > 0) { // 만약 지웠는데 성공했다면
				System.out.println("helloword111");
				hotplaceService.minusLikeCnt(map.get("num")); // 좋아요 수 감소시키고,
				return new ResponseEntity<String>(SUCCESS, HttpStatus.OK); // return
			}else { // 좋아요가 등록 안되어 있었던 거니깐
				hotplaceService.insertLike(map); // like 테이블에 추가시키고
				System.out.println("helloword");
				hotplaceService.plusLikeCnt(map.get("num")); // 해당 번호에 해당하는 핫플게시글의 좋아요 수 증가
				return new ResponseEntity<String>(SUCCESS, HttpStatus.OK); // return
			}

		}catch(Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "핫플레이스 삭제", notes = "글 번호에 해당하는 핫플레이스 삭제", response = String.class)
	@DeleteMapping("/{num}")
	public ResponseEntity<?> delete(@PathVariable("num") String num) throws SQLException {
		logger.debug("delete hotplace : {}", num);
		
		int result = hotplaceService.deleteHotplace(num);
		if(result == 1) {
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
}
