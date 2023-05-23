package com.ssafy.user.model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
	private Map<String, Integer> authInfo = new HashMap<>();

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
	public MailDto authEmailAddress(String emailId, String emailDomain) throws SQLException {
		
		Random random = new Random();		//랜덤 함수 선언
		int createNum = 0;  			//1자리 난수
		String ranNum = ""; 			//1자리 난수 형변환 변수
        int letter    = 6;			//난수 자릿수:6
		String resultNum = "";  		//결과 난수
		
		for (int i=0; i<letter; i++) { 
			createNum = random.nextInt(9);		//0부터 9까지 올 수 있는 1자리 난수 생성
			ranNum =  Integer.toString(createNum);  //1자리 난수를 String으로 형변환
			resultNum += ranNum;			//생성된 난수(문자열)을 원하는 수(letter)만큼 더하며 나열
		}	
		authInfo.put(emailId+"@"+emailDomain, Integer.parseInt(resultNum));
		
		MailDto mailDto = new MailDto();
		mailDto.setAddress(emailId + "@" + emailDomain);
		mailDto.setTitle("Trip or Trip! 이메일 인증 확인 이메일입니다.");
		mailDto.setMessage("안녕하세요. Trip or Trip! 이메일 인증번호 안내 관련 이메일입니다.\n 회원님의 인증번호는 "
                + resultNum + " 입니다.\n");
		
		return mailDto;
	}
	
	@Override
	public boolean checkAuthNumber(String email, int number) throws SQLException{
		if(authInfo.get(email) == number) return true;
		return false;
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

	@Override
	public int uploadProfile(String userId, String saveFileName) throws SQLException {
		return userMapper.uploadProfile(userId, saveFileName);
	}

}
