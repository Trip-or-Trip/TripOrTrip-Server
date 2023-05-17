package com.ssafy.hotplace.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
import com.ssafy.user.model.UserDto;

import io.swagger.annotations.Api;


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
	
	@Value("${file.path.hotplacePath}")
	private String hotplacePath;
	
	private HotplaceService hotplaceService;

	public HotplaceController(HotplaceService hotplaceService) {
		super();
		this.hotplaceService = hotplaceService;
	}

	@GetMapping("/list")
	public ResponseEntity<?> list(Model model) throws SQLException {
		logger.debug("hotplace list");
		
		List<HotplaceDto> list = hotplaceService.listHotplace();
		return new ResponseEntity<List<HotplaceDto>>(list, HttpStatus.OK);
	}
	
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
	
	@GetMapping("/{num}")
	public ResponseEntity<?> view(@PathVariable("num") String num) throws SQLException {
		HotplaceDto hotplaceDto = hotplaceService.viewHotplace(num);
		
		return new ResponseEntity<HotplaceDto>(hotplaceDto, HttpStatus.OK);
	}
	
	@PutMapping("")
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
