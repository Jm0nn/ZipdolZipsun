package com.ssafy.zip.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.zip.user.model.UserDto;

// 로그인 여부 확인을 위한 인터셉터
@Component
public class ConfirmInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 세션 객체를 얻어와 로그인 정보가 있는지 확인
		HttpSession session = request.getSession();

		UserDto memberDto = (UserDto) session.getAttribute("loginUser");
		if (memberDto == null) {
			response.sendRedirect(request.getContextPath() + "/member/login");
			return false; // 컨트롤러로 request, response 객체를 넘기지 않음
		}

		return true; // 이전과 동일하게 컨트롤러로 request, response 객체를 넘김
	}

}
