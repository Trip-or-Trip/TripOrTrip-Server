package com.ssafy.config;

import java.util.Arrays;
import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.interceptor.ConfirmInterceptor;
import com.ssafy.interceptor.JwtInterceptor;

@Configuration
@MapperScan(basePackages = {"com.ssafy.**.mapper"})
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer{
//	private final List<String> patterns = Arrays.asList("/hotplace/**", "/plan/**", "/board/**");
//	private final List<String> excludePatterns = Arrays.asList("/hotplace/list", "/hotplace/image/**", "/plan/mvplanlist", "/board/list", "/board/view");
	private final List<String> patterns = Arrays.asList("/board/**", "/hotplace/**", "notice/**", "/plan/**");
	private final List<String> excludePatterns = Arrays.asList("/board/list", "/hotplace/list", "notice/list", "/plan/list");
	
	private ConfirmInterceptor confirmInterceptor;
	private JwtInterceptor jwtInterceptor;
	
	public WebMvcConfiguration(ConfirmInterceptor confirmInterceptor, JwtInterceptor jwtInterceptor) {
		this.confirmInterceptor = confirmInterceptor;
		this.jwtInterceptor = jwtInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(confirmInterceptor).addPathPatterns(patterns);
		registry.addInterceptor(jwtInterceptor)
        .addPathPatterns(patterns)
        .excludePathPatterns(excludePatterns);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
//		System.out.println("CORS Setting");
//		default 설정.
//		Allow all origins.
//		Allow "simple" methods GET, HEAD and POST.
//		Allow all headers.
//		Set max age to 1800 seconds (30 minutes).
		registry.addMapping("/**").allowedOrigins("*")
//		.allowedOrigins("http://localhost:8080", "http://localhost:8081")
			.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
					HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
					HttpMethod.PATCH.name())
//			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
			.exposedHeaders("Authorization")
			.maxAge(1800);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/hotplace/image/**")
                .addResourceLocations("file:///C:/trip/upload/hotplace/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
	
}