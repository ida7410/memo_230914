package com.memo.interceptor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// Controller 수행 여부 결정
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object Handler) throws IOException {
		
		 
		// 요청 url path
		String uri = request.getRequestURI();
		logger.info("[@@@@@@ preHandle] uri:{}", uri);
		
		// 로그인 여부
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		// 비로그인 + /post => 로그인 페이지로 이동, controller x
		if (userId == null && uri.startsWith("/post/")) {
			response.sendRedirect("/user/sign-in-view");
			return false; // controller 방지
		}
		
		// 로그인 + /user => 글 목록 페이지로 이동, controller x
		if (userId != null && uri.startsWith("/user/")) {
			response.sendRedirect("/post/post-list-view");
			return false;
		}
		
		return true;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, 
			HttpServletResponse response, Object Handler,
			ModelAndView mav) {
		
		// ModelAndView = model과 view가 함께 담겨 있음
		// = view 객체가 존재함 -> jsp가 html로 변환되기 전
		log.debug("[###### postHandle]");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, 
			HttpServletResponse response, Object Handler,
			Exception ex) {
		
		// jsp가 html로 최종 변환된 후
		log.debug("[$$$$$$ afterCompletion]");
	}
}
