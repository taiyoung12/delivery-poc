package com.barogo.java.delivery.poc.common.config.app;

import static com.barogo.java.delivery.poc.common.config.app.path.ApiPath.Auth.*;
import static com.barogo.java.delivery.poc.common.config.app.path.ApiPath.Delivery.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.barogo.java.delivery.poc.common.interceptor.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final JwtInterceptor jwtInterceptor;

	public WebConfig(JwtInterceptor jwtInterceptor) {
		this.jwtInterceptor = jwtInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
			.addPathPatterns(DELIVERY_ALL)
			.excludePathPatterns(AUTH_ALL);
	}
}

