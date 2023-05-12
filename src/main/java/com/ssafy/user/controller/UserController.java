package com.ssafy.user.controller;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
//	private RSAUtil rsaUtil;

	private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	private static String RSA_INSTANCE = "RSA"; // rsa transformation

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	/**
	 * 복호화
	 */
	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
		Cipher cipher = Cipher.getInstance(RSA_INSTANCE);
		byte[] encryptedBytes = hexToByteArray(securedValue);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의
		return decryptedValue;
	}

	/**
	 * 16진 문자열을 byte 배열로 변환
	 */
	public byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[] {};
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}

	/**
	 * rsa 공개키, 개인키 생성
	 */
	public Map<String, String> initRsa(HttpSession session) {
		KeyPairGenerator generator;
		try {
			generator = KeyPairGenerator.getInstance(RSA_INSTANCE);
			generator.initialize(1024);

			KeyPair keyPair = generator.genKeyPair();
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_INSTANCE);
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			
			if(session.getAttribute(RSA_WEB_KEY) != null) {
				session.removeAttribute(RSA_WEB_KEY);
			}
			session.setAttribute(RSA_WEB_KEY, privateKey);

			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			String publicKeyModulus = publicSpec.getModulus().toString(16);
			String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

			Map<String, String> map = new HashMap<String, String>();
			map.put("RSAModulus", publicKeyModulus); // rsa modulus 를 request 에 추가
			map.put("RSAExponent", publicKeyExponent); // rsa exponent 를 request 에 추가
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/auth")
	@ApiOperation(value = "입력된 사용자 정보를 이용해 로그인한다.", response = UserDto.class)
	public ResponseEntity<?> signin(@RequestBody @ApiParam(value = "로그인한 회원 정보", required = true) UserDto userDto, HttpSession session) throws SQLException {
		logger.debug("login user: {}", userDto.toString());
		
		UserDto user = userService.signinUser(userDto.getId(), userDto.getPassword());
		if(user != null) {
			session.setAttribute("userinfo", user);
			return new ResponseEntity<UserDto>(user, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
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
