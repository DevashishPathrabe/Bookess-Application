package com.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.assignment.interceptor.AuthorizationInterceptor;

@Configuration
@EnableWebMvc
public class AuthorizationConfig implements WebMvcConfigurer {
	
	@Bean
	public AuthorizationInterceptor authorizationInterceptor() {
	    return new AuthorizationInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor()).addPathPatterns("/**");
	}

}
