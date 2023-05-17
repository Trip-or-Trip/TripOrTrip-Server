package com.ssafy.user.model.service;

import java.sql.SQLException;

import com.ssafy.mail.model.MailDto;
import com.ssafy.user.model.UserDto;

public interface UserService {
	UserDto signinUser(String id, String password) throws SQLException;
	int signupUser(UserDto userDto) throws SQLException;
	int deleteUser(String id) throws SQLException;
	int updateUser(UserDto userDto) throws SQLException;
	int idCheck(String id) throws SQLException;
	int emailCheck(String emailId, String emailDomain) throws SQLException;
	UserDto findUserById(String id) throws SQLException;
	
	UserDto findUser(String id, String emailId, String emailDomain) throws SQLException;
	MailDto createMailAndChangePassword(String id, String emailId, String emailDomain) throws SQLException;
	String getTempPassword();
	void sendEmail(MailDto mailDto);
}
