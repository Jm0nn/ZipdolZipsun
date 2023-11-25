package com.ssafy.zip.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.zip.interceptor.ConfirmInterceptor;

@Configuration
@EnableAspectJAutoProxy
@MapperScan(basePackages = { "com.ssafy.zip.*.model.mapper" })
public class WebMvcConfiguration implements WebMvcConfigurer {

//	private final String uploadFilePath;

//	public WebMvcConfiguration(@Value("${file.path.upload-files}") String uploadFilePath) {
//		this.uploadFilePath = uploadFilePath;
//	}

	// 인터셉터
	@Autowired
	private ConfirmInterceptor confirmInterceptor;

	// 아파트 거래 정보 검색 페이지로 이동 시 로그인 필요
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(confirmInterceptor).addPathPatterns("/house");
	}

	// CORS
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
				.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
						HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
						HttpMethod.PATCH.name())
				.maxAge(1800);
	}

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/upload/file/**").addResourceLocations("file:///" + uploadFilePath + "/")
//				.setCachePeriod(3600).resourceChain(true).addResolver(new PathResourceResolver());
//	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}

}
