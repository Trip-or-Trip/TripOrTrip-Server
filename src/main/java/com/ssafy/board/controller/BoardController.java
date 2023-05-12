package com.ssafy.board.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.board.model.service.BoardService;
import com.ssafy.user.model.UserDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/board")
@CrossOrigin("*")
public class BoardController {

	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	private BoardService boardService;
	
	@Autowired
	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	@ApiOperation(value = "게시판 글목록", notes = "모든 게시글의 정보를 반환한다.", response = List.class)
	@GetMapping(value = "/")
	private ResponseEntity<?> listArticle(@ApiParam(value = "게시글을 얻기위한 부가정보.", required = true)  BoardParameterDto boardParameterDto) {
		logger.debug("boardList call");
		try {
			List<BoardDto> list = boardService.listArticle(boardParameterDto);
//			mav.addObject("articles", list);
//			PageNavigation pageNavigation = boardService.makePageNavigation(map);
//			mav.addObject("navigation", pageNavigation);
//			mav.addObject("pgno", map.get("pgno"));
//			mav.addObject("key", map.get("key"));
//			mav.addObject("word", map.get("word"));
//			mav.setViewName("board/list");
//			return mav;
			return new ResponseEntity<List<BoardDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
//			e.printStackTrace();
//			mav.addObject("msg", "글목록 출력 중 문제 발생!!!");
//			mav.setViewName("error/error");
//			return mav;
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "게시판 글보기", notes = "글번호에 해당하는 게시글의 정보를 반환한다.", response = BoardDto.class)
	@GetMapping(value = "/{articleno}")
	private ResponseEntity<?> getArticle(@PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleNo, @RequestParam Map<String, String> map) {
		try {
			logger.info("getArticle - 호출 : " + articleNo);
			BoardDto boardDto = boardService.getArticle(articleNo);
			if(boardDto != null) {
				boardService.updateHit(articleNo);
				return new ResponseEntity<BoardDto>(boardService.getArticle(articleNo), HttpStatus.OK); 
			}else {
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
//			e.printStackTrace();
//			model.addAttribute("msg", "글내용 출력 중 문제 발생!!!");
//			return "/error/error";
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "게시판 글작성", notes = "새로운 게시글 정보를 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping(value = "/")
//	private String write(BoardDto boardDto, HttpSession session, Model model) {
	private ResponseEntity<?> writeArticle(@RequestBody @ApiParam(value = "게시글 정보.", required = true) BoardDto boardDto, HttpSession session){
		logger.debug("BoardController: writeAricle - 호출");
		UserDto userDto = (UserDto) session.getAttribute("userinfo");
		boardDto.setUserId(userDto.getId());
		logger.debug(boardDto.toString());
		try {
			boardService.writeArticle(boardDto);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
//			e.printStackTrace();
//			model.addAttribute("msg", "글 작성 중 에러 발생!");
//			return "/error/error";
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
		
	}
	
	@ApiOperation(value = "게시판 글수정", notes = "수정할 게시글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping("/")
	private ResponseEntity<String> modifyArticle(@RequestBody @ApiParam(value = "수정할 글정보.", required = true) BoardDto boardDto) {
		logger.debug("BoardController: modifyAricle - 호출");
		try {
			boardService.modifyArticle(boardDto);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
//			e.printStackTrace();
//			model.addAttribute("msg", "게시글 수정 중 에러 발생");
			return new ResponseEntity<String>(FAIL, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "게시판 글삭제", notes = "글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("/{articleno}")
	private ResponseEntity<String> deleteArticle(@PathVariable("articleno") @ApiParam(value = "살제할 글의 글번호.", required = true) int articleNo) {
		try {
			boardService.deleteArticle(articleNo);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e1) {
//			e1.printStackTrace();
//			model.addAttribute("msg", "게시글이 삭제되었거나 존재하지 않습니다.");
//			return "error/error";
			return new ResponseEntity<String>(FAIL, HttpStatus.OK);
		}
		
	}
	
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}