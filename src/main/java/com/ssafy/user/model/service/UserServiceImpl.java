package com.ssafy.user.model.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ssafy.mail.model.MailDto;
import com.ssafy.user.model.UserDto;
import com.ssafy.user.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromAddress;

	private UserMapper userMapper;

	public UserServiceImpl(UserMapper userMapper) {
		super();
		this.userMapper = userMapper;
	}
	
	@Override
	public UserDto signinUser(String id, String password) throws SQLException {
		return userMapper.signinUser(id, password);
	}

	@Override
	public int signupUser(UserDto userDto) throws SQLException {
		return userMapper.signupUser(userDto);
	}

	@Override
	public int deleteUser(String id) throws SQLException {
		return userMapper.deleteUser(id);
	}

	@Override
	public int updateUser(UserDto userDto) throws SQLException {
		return userMapper.updateUser(userDto);
	}

	@Override
	public int idCheck(String id) throws SQLException {
		return userMapper.idCheck(id);
	}
	
	@Override
	public int emailCheck(String email, String emailDomain) throws SQLException {
		return userMapper.emailCheck(email, emailDomain);
	}
	
	@Override
	public UserDto findUserById(String id) throws SQLException {
		return userMapper.findUserById(id);
	}
	
	@Override
	public UserDto findUser(String id, String emailId, String emailDomain) throws SQLException {
		return userMapper.findUser(id, emailId, emailDomain);
	}

	@Override
	public MailDto createMailAndChangePassword(String id, String emailId, String emailDomain) throws SQLException {
		String tempPassword = getTempPassword();
		MailDto mailDto = new MailDto();
		mailDto.setAddress(emailId + "@" + emailDomain);
		mailDto.setTitle("Trip or Trip! 임시 비밀번호 안내 이메일입니다.");
		mailDto.setMessage("안녕하세요. Trip or Trip! 임시 비밀번호 안내 관련 이메일입니다.\n회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다.\n로그인 후에 비밀번호를 변경해 주세요.");
		
		userMapper.updatePassword(id, tempPassword);
		return mailDto;
		
	}

	@Override
	public String getTempPassword() {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
	}

	@Override
	public void sendEmail(MailDto mailDto) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(fromAddress);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        System.out.println("message:" + message);
        mailSender.send(message);
	}

}
