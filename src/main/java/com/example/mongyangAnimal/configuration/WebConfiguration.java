package com.example.mongyangAnimal.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	// CORS(cross origin resource sharing)을 설정
	// 서로 다른 도메인: 포트 간의 통신을 허용을 해줌
	// AJAX(async javascript and XML) 통신 허용
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	// 모든 경로 (/...)					//모든 메소드(GET,POST....)
        registry.addMapping("/**").allowedMethods("*");
    }
    
//    @Bean
//    public AuthInterceptor requestHandler() {
//    	return new AuthInterceptor();
//    }
//    
//    // 인터셉터를 추가
//    @Override
//    public void addInterceptors(final InterceptorRegistry registry) {
//    	registry.addInterceptor(requestHandler());
    }



