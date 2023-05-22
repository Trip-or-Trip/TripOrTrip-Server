package com.ssafy.board.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.BoardParameterDto;
import com.ssafy.board.model.service.BoardService;
import com.ssafy.comment.model.CommentDto;
import com.ssafy.user.model.UserDto;
import com.ssafy.util.PageNavigation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/board")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
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
	@GetMapping("/list")
	private ResponseEntity<?> listArticle() {
		BoardParameterDto boardParameterDto = new BoardParameterDto();
		logger.debug("boardList call");
		try {
			List<BoardDto> list = boardService.listArticle(boardParameterDto);
			return new ResponseEntity<List<BoardDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "게시판 글목록", notes = "모든 게시글의 정보를 반환한다.", response = List.class)
	@PostMapping("/list")
	private ResponseEntity<?> listArticle(@ApiParam(value = "게시글을 얻기위한 부가정보.", required = true) @RequestBody BoardParameterDto boardParameterDto) {
		logger.debug("boardList call => key: {}, word: {}", boardParameterDto.getKey(), boardParameterDto.getWord());
		try {
			List<BoardDto> list = boardService.listArticle(boardParameterDto);
			return new ResponseEntity<List<BoardDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "게시판 글보기", notes = "글번호에 해당하는 게시글의 정보를 반환한다.", response = BoardDto.class)
	@PostMapping("/{articleno}")
	private ResponseEntity<?> getArticle(@PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleNo) {
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
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "게시판 댓글 보기", notes = "글번호에 해당하는 게시글의 댓글 정보를 반환한다.", response = List.class)
	@PostMapping("/comment/{articleno}")
	private ResponseEntity<?> getCommnet(@PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleNo, @RequestBody String userId) {
		try {
			logger.info("BoardController:: getComment - 호출 : " + articleNo);
			List<CommentDto> list = boardService.getComment(articleNo);
			if(list != null) {
				return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK); 
			}else {
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "내가쓴 댓글 보기", notes = "내가 쓴 댓글 정보를 반환한다.", response = List.class)
	@PostMapping("/comment")
	private ResponseEntity<?> getCommnetList(@RequestBody @ApiParam(value = "얻어올 사용자 ID", required = true) String userId) {
		try {
			logger.info("BoardController:: getCommentList - 호출 : " + userId);
			userId = userId.substring(1,  userId.length()-1);
			List<CommentDto> list = boardService.getCommentList(userId);
			if(list != null) {
				return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK); 
			}else {
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "게시판 댓글 쓰기", notes = "글번호에 해당하는 게시글의 댓글을 작성한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping("/writecomment/{articleno}")
	private ResponseEntity<?> writeCommnet(@PathVariable("articleno") @ApiParam(value = "작성할 글의 글번호.", required = true) int articleNo, @RequestBody CommentDto comment) {
		try {
			logger.info("BoardController:: writeComment - 호출 : " + articleNo);
			comment.setBoardId(articleNo);
			boardService.writeComment(comment);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK); 
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation(value = "게시판 댓글 삭제", notes = "글번호에 해당하는 게시글의 댓글을 삭제한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("/deletecomment/{articleno}")
	private ResponseEntity<?> deleteCommnet(@PathVariable("articleno") @ApiParam(value = "작성할 글의 글번호.", required = true) int articleNo) {
		try {
			logger.info("BoardController:: deleteComment - 호출 : " + articleNo);
			boardService.deleteComment(articleNo);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK); 
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation(value = "게시판 글작성", notes = "새로운 게시글 정보를 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping("/write")
	private ResponseEntity<?> writeArticle(@RequestBody @ApiParam(value = "게시글 정보.", required = true) BoardDto boardDto){
		logger.debug("BoardController: writeAricle - 호출");
//		UserDto userDto = (UserDto) session.getAttribute("userinfo");
//		boardDto.setUserId(userDto.getId());
		
		try {
			boardService.writeArticle(boardDto);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation(value = "게시판 글수정", notes = "수정할 게시글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping("")
	private ResponseEntity<String> modifyArticle(@RequestBody @ApiParam(value = "수정할 글정보.", required = true) BoardDto boardDto) {
		logger.debug("BoardController: modifyAricle - 호출");
		try {
			boardService.modifyArticle(boardDto);
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "게시판 글삭제", notes = "글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("/{articleno}")
	private ResponseEntity<String> deleteArticle(@RequestBody  @PathVariable("articleno") @ApiParam(value = "살제할 글의 글번호.", required = true) int articleNo) {
		try {
			boardService.deleteArticle(articleNo);
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