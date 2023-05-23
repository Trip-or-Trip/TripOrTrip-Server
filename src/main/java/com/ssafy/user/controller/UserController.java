package com.ssafy.user.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssafy.exception.UnauthorizedException;
import com.ssafy.jwt.JwtService;
import com.ssafy.mail.model.MailDto;
import com.ssafy.user.model.UserDto;
import com.ssafy.user.model.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
@Api("유저 컨트롤러  API V1")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	private UserService userService;
	private JwtService jwtService;

	public UserController(UserService userService, JwtService jwtService) {
		super();
		this.userService = userService;
		this.jwtService = jwtService;
	}
	
	@GetMapping(path = "/token")
	@ApiOperation(value = "토큰 정보를 바탕으로 사용자 정보를 구한다.", response = UserDto.class)
	public ResponseEntity<?> token(@RequestHeader("Authorization") String token) throws SQLException {
		token = token.split(" ")[1];
		String id = jwtService.getSubject(token);
		
		logger.debug("token: {}, id: {}", token, id);
		
		UserDto userDto = userService.findUserById(id);
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}
		
	
	@PostMapping("/auth")
	@ApiOperation(value = "입력된 사용자 정보를 이용해 로그인한다.", response = UserDto.class)
	public String signin(@RequestBody @ApiParam(value = "로그인한 회원 정보", required = true) UserDto userDto, HttpServletResponse response) throws SQLException {
		logger.debug("login user: {}", userDto.toString());
		
		UserDto user = userService.signinUser(userDto.getId(), userDto.getPassword());
		if(user != null) {
			String token = jwtService.createToken(user.getId() + "", (60 * 1000 * 60));
			System.out.println(token);
			response.setHeader("authorization", token);
			return token;
//			return new ResponseEntity<UserDto>(user, HttpStatus.OK);
		}
		else {
//			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			new UnauthorizedException();
			return null;
		}
	}
	
	@GetMapping("/auth")
	@ApiOperation(value = "로그인된 사용자를 로그아웃한다.", response = Integer.class)
	public ResponseEntity<?> signout(HttpSession session) {
		logger.debug("logout user");
		
		session.removeAttribute("userinfo");
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	@PostMapping("")
	@ApiOperation(value = "사용자 정보를 삽입한다.", response = Integer.class)
	public ResponseEntity<?> signup(@RequestBody UserDto userDto) throws SQLException {
		logger.debug("create user: {}", userDto.toString());
		
		int result = userService.signupUser(userDto);
		if(result == 1) {
			return new ResponseEntity<Integer>(result, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "사용자 정보를 수정한다.", response = Integer.class)
	public ResponseEntity<?> update(@RequestBody UserDto userDto) throws Exception {
		logger.debug("update user: {}", userDto.toString());
		
		int result = userService.updateUser(userDto);
		if(result == 1) {
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "입력된 사용자 정보를 삭제한다.", response = Integer.class)
	public ResponseEntity<?> delete(@PathVariable("id") String id, HttpSession session) throws Exception {
		logger.debug("delete user : {}", id);
		
		int result = userService.deleteUser(id);
		if(result == 1) {
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/id/{id}")
	@ApiOperation(value = "입력된 아이디의 중복 여부를 체크한다.", response = Integer.class)
	public String idCheck(@PathVariable("id") String id) throws Exception {
		logger.debug("idCheck userid : {}", id);
		
		int cnt = userService.idCheck(id);
		return cnt + "";
	}
	
	@GetMapping("/email/{emailId}/{emailDomain}")
	@ApiOperation(value = "입력된 이메일의 중복 여부를 체크한다.", response = Integer.class)
	public String emailCheck(@PathVariable("emailId") String emailId, @PathVariable("emailDomain") String emailDomain) throws Exception {
		logger.debug("emailCheck userid : {}@{}", emailId, emailDomain);
		
		int cnt = userService.emailCheck(emailId, emailDomain);
		return cnt + "";
	}
	
	@GetMapping("/auth/{emailId}/{emailDomain}")
	@ApiOperation(value = "입력된 이메일로 인증번호를 전송한다.", response = Integer.class)
	public String  sendEmailAuthCheck(@PathVariable("emailId") String emailId, @PathVariable("emailDomain") String emailDomain) throws Exception {
		logger.debug("sendEmailAuthCheck sending email to : {}@{}", emailId, emailDomain);
		
		MailDto mail = userService.authEmailAddress(emailId, emailDomain);
		userService.sendEmail(mail);
		return "success";
	}
	
	@GetMapping("/check/{emailId}/{emailDomain}/{number}")
	@ApiOperation(value = "입력된 이메일로 전송된 인증번호를 검증한다.", response = Integer.class)
	public boolean  emailAuthCheck(@PathVariable("emailId") String emailId, @PathVariable("emailDomain") String emailDomain, @PathVariable("number") int number) throws Exception {
		logger.debug("emailAuthCheck 해당 메일로 보낸 인증번호 확인: {}@{}", emailId, emailDomain);
		return userService.checkAuthNumber(emailId+"@"+emailDomain, number);
	}
	
	@GetMapping("/password/{id}/{emailId}/{emailDomain}")
	@Transactional
	public ResponseEntity<?> password(@PathVariable("id") String id, @PathVariable("emailId") String emailId, @PathVariable("emailDomain") String emailDomain, RedirectAttributes redirectAttributes) throws SQLException {
		logger.debug("find password : {}, {}, {}", id, emailId, emailDomain);
		
		UserDto userDto = userService.findUser(id, emailId, emailDomain);
		
		if(userDto == null) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		else {
			MailDto mailDto = userService.createMailAndChangePassword(userDto.getId(), emailId, emailDomain);
			userService.sendEmail(mailDto);
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		}
	}
}
