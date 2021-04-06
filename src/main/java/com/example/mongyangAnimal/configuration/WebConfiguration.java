package com.example.mongyangAnimal.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 모든 경로 (/...) //모든 메소드(GET,POST....)
		registry.addMapping("/**").allowedMethods("*");
	}

}
