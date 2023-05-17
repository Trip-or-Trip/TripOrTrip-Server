package com.ssafy.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.exception.UnauthorizedException;
import com.ssafy.jwt.JwtService;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final String HEADER_AUTH = "Authorization";

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HEADER_AUTH);
        System.out.println(token);
        if(token != null) {
        	token = token.split(" ")[1];
            System.out.println(token);
        	if(jwtService.isUsable(token)) {
        		return true;
        	}
        	else {
        		throw new UnauthorizedException();
        	}
        } else {
            throw new UnauthorizedException();
        }
    }
}