package com.ssafy.notice.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import org.springframework.web.servlet.ModelAndView;

import com.ssafy.board.controller.BoardController;
import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.notice.model.NoticeDto;
import com.ssafy.notice.model.service.NoticeService;
import com.ssafy.user.model.UserDto;
import com.ssafy.util.PageNavigation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/notice")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class NoticeController {

	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	private NoticeService noticeService;
	
	@Autowired
	public NoticeController(NoticeService noticeService) {
		super();
		this.noticeService = noticeService;
	}

	@ApiOperation(value = "공지사항 글목록", notes = "모든 공지사항의 정보를 반환한다.", response = List.class)
	@GetMapping("/list")
	private ResponseEntity<?> listArticle(@ApiParam(value = "공지사항 얻기위한 부가정보.", required = true)  BoardParameterDto boardParameterDto) {
		logger.debug("NoticeController:: listArticle call");
		try {
			List<NoticeDto> list = noticeService.listArticle(boardParameterDto);
			return new ResponseEntity<List<NoticeDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "공지사항 글목록", notes = "모든 공지사항의 정보를 반환한다.", response = List.class)
	@PostMapping("/list")
	private ResponseEntity<?> listKeywordArticle(@ApiParam(value = "공지사항 얻기위한 부가정보.", required = true) @RequestBody BoardParameterDto boardParameterDto) {
		logger.debug("NoticeController:: listArticle keyword call");
		try {
			List<NoticeDto> list = noticeService.listArticle(boardParameterDto);
			return new ResponseEntity<List<NoticeDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "공지사항 상세보기", notes = "글번호에 해당하는 공지사항의 정보를 반환한다.", response = NoticeDto.class)
	@GetMapping("/{articleno}")
	private ResponseEntity<?> getArticle(@PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleNo, @RequestParam Map<String, String> map) {
		try {
			logger.debug("NoticeController:: getArticle - 호출 : " + articleNo);
			NoticeDto noticeDto = noticeService.getArticle(articleNo);
			if(noticeDto != null) {
				noticeService.updateHit(articleNo);
				return new ResponseEntity<NoticeDto>(noticeService.getArticle(articleNo), HttpStatus.OK); 
			}else {
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "공지사항 작성", notes = "새로운 공지사항을 작성한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping("/write")
	private ResponseEntity<?> writeArticle(@RequestBody @ApiParam(value = "게시글 정보.", required = true) NoticeDto noticeDto, HttpSession session){
		logger.debug("NoticeController: writeAricle - 호출");
//		UserDto userDto = (UserDto) session.getAttribute("userinfo");
//		noticeDto.setUserId(userDto.getId());
		try {
			noticeService.writeArticle(noticeDto);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
		
	}

	@ApiOperation(value = "공지사항 수정", notes = "수정할 공지사항 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping("")
	private ResponseEntity<String> modifyArticle(@RequestBody @ApiParam(value = "수정할 공지사항 정보.", required = true) NoticeDto noticeDto) {
		logger.debug("NoticeController: modifyAricle - 호출");
		try {
			noticeService.modifyArticle(noticeDto);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "공지사항 삭제", notes = "글번호에 해당하는 공지사항 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("/{articleno}")
	private ResponseEntity<String> deleteArticle(@PathVariable("articleno") @ApiParam(value = "살제할 글의 글번호.", required = true) int articleNo) {
		try {
			System.out.println(articleNo);
			noticeService.deleteArticle(articleNo);
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