package com.ssafy.user.model.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.user.model.UserDto;

@Mapper
public interface UserMapper {
	UserDto signinUser(String id, String password) throws SQLException;
	int signupUser(UserDto userDto) throws SQLException;
	int deleteUser(String id) throws SQLException;
	int updateUser(UserDto userDto) throws SQLException;
	int idCheck(String id) throws SQLException;
	int emailCheck(String emailId, String emailDomain) throws SQLException;
	UserDto findUserById(String id) throws SQLException;
	
	UserDto findUser(String id, String emailId, String emailDomain) throws SQLException;
	int updatePassword(String id, String tempPassword) throws SQLException;
	
	int uploadProfile(String userId, String saveFileName) throws SQLException;
}
