package com.ssafy.jwt;

public interface JwtService {
	String createToken(String subject, long time);
	String getSubject(String token);
	boolean isUsable(String jwt);
}
