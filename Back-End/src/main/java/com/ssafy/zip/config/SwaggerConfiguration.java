package com.ssafy.zip.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Component("swagger2Config")
@Configuration // @Bean을 이용하여 빈 객체 생성을 위해 추가
@EnableSwagger2 // 스웨거2에서 아래 내용을 불러오기 위한 애노테이션 추가
public class SwaggerConfiguration {

	// Swagger 3
	// http://localhost/swagger-ui/index.html

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.consumes(getConsumeContentTypes())
				.produces(getProduceContentTypes())
				.apiInfo(apiInfo())
				.groupName("V1") // Docket 객체를 구별하는 고유 값 작성
				.select() // 어떤 RestController를 가지고 탐색할 것인지 조건 작성을 위한 객체 리턴
				.apis(RequestHandlerSelectors.basePackage("com.ssafy.zip.admin.controller")) // 대상 패키지명
				.paths(PathSelectors.regex("/admin/.*")) // 정규식에 해당하는 경로의 API를 추가
				.build()
				.useDefaultResponseMessages(false); // 기본 응답 메시지 사용 유무
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Happy House API V1")
				.description("<h3>SSAFY API</h3>Swagger를 이용한 Happy House API<br/>"
						+ "<img src=\"/assets/image/ssafy_logo.png\" width=\"150\"/>")
				.contact(new Contact("SSAFY", "https://edu.ssafy.com", "ssafy@ssafy.com"))
				.license("SSAFY license")
				.licenseUrl("https://www.ssafy.com/ksp/jsp/swp/etc/swpPrivacy.jsp")
				.version("1.0")
				.build();
	}

	private Set<String> getProduceContentTypes() {
		Set<String> produces = new HashSet<>();
		produces.add("application/json;charset=utf-8"); // API 테스트 시, 응답을 JSON으로 함
		return produces;
	}

	private Set<String> getConsumeContentTypes() {
		Set<String> consumes = new HashSet<>();
		consumes.add("application/json;charset=utf-8"); // API 테스트 시, JSON 요청을 받음
		consumes.add("application/x-www-form-urlencoded"); // API 테스트 시, form 태그 요청을 받음
		return consumes;
	}

}
