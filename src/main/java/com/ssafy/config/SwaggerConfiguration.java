package com.ssafy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@SuppressWarnings("unchecked") // warning 제거
public class SwaggerConfiguration {

	// Swagger-UI 2.x 확인
	// http://localhost[:8080]/{your-app-root}/swagger-ui.html
	// Swagger-UI 3.x 확인
	// http://localhost[:8080]/{your-app-root}/swagger-ui/index.html

	private String version = "V1";
	private String title = "Trip or Trip API " + version;

	private ApiInfo apiInfo() {
		String descript = "Trip or Trip Vuejs API Reference for Developers<br>";
		return new ApiInfoBuilder().title(title).description(descript)
				// .termsOfServiceUrl("https://edu.ssafy.com")
				.contact(new Contact("admin", "https://github.com/Trip-or-Trip", "gunhoo2016@gmail.com")).license("Trip or Trip License")
				.licenseUrl("gunhoo2016@gmail.com").version("1.0").build();
	}

	// API마다 구분짓기 위한 설정.
	@Bean
	public Docket userApi() {
		return getDocket("회원", Predicates.or(PathSelectors.regex("/user.*")));
	}

	@Bean
	public Docket boardApi() {
		return getDocket("게시판", Predicates.or(PathSelectors.regex("/board.*")));
	}

	@Bean
	public Docket noticeApi() {
		return getDocket("공지사항", Predicates.or(PathSelectors.regex("/notice.*")));
	}

	@Bean
	public Docket touristApi() {
		return getDocket("지역별 검색", Predicates.or(PathSelectors.regex("/tourist.*")));
	}

	@Bean
	public Docket planApi() {
		return getDocket("여행계획", Predicates.or(PathSelectors.regex("/plan.*")));
	}
	
	@Bean
	public Docket hotplaceApi() {
		return getDocket("핫플레이스", Predicates.or(PathSelectors.regex("/hotplace.*")));
	}
	
	@Bean
	public Docket mypageApi() {
		return getDocket("마이페이지", Predicates.or(PathSelectors.regex("/mypage.*")));
	}

	public Docket getDocket(String groupName, Predicate<String> predicate) {
		// List<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>();
		// responseMessages.add(new ResponseMessageBuilder().code(200).message("OK
		// !!!").build());
		// responseMessages.add(new ResponseMessageBuilder().code(500).message("서버 문제 발생
		// !!!").responseModel(new ModelRef("Error")).build());
		// responseMessages.add(new ResponseMessageBuilder().code(404).message("페이지를 찾을
		// 수 없습니다 !!!").build());
		return new Docket(DocumentationType.SWAGGER_2).groupName(groupName).apiInfo(apiInfo()).select()
				// .apis(RequestHandlerSelectors.basePackage("com.ssafy.user.controller"))
				// .paths(predicate)
				.apis(RequestHandlerSelectors.any())
				.paths(predicate)
				.build();
		// .useDefaultResponseMessages(false)
		// .globalResponseMessage(RequestMethod.GET,responseMessages);
	}

	// swagger ui 설정.
	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder().displayRequestDuration(true).validatorUrl("").build();
	}

}