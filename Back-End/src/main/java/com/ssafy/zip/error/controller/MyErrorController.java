package com.ssafy.zip.error.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyErrorController extends BasicErrorController {

	public MyErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
			List<ErrorViewResolver> errorViewResolvers) {

		super(errorAttributes, serverProperties.getError(), errorViewResolvers);
	}

	@Override
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = super.errorHtml(request, response);

		HttpStatus hs = getStatus(request);
		switch (hs) {
		case NOT_FOUND:
			mnv.addObject("msg", "해당 페이지를 찾을 수 없습니다!!!");
			break;

		default: // 상태 코드 500
			mnv.addObject("msg", "처리 중 에러 발생!!!");
			break;
		}
		
		mnv.setViewName("/error/error");
		return mnv;
	}

}