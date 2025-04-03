package com.barogo.java.delivery.poc.common.config.app;

import static com.barogo.java.delivery.poc.common.config.app.path.ApiPath.Auth.*;
import static com.barogo.java.delivery.poc.common.config.app.path.ApiPath.Delivery.*;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.barogo.java.delivery.poc.common.config.app.annotation.UserEmailResolver;
import com.barogo.java.delivery.poc.common.interceptor.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final JwtInterceptor jwtInterceptor;
	private final UserEmailResolver userEmailResolver;


	public WebConfig(JwtInterceptor jwtInterceptor, UserEmailResolver userEmailResolver) {
		this.jwtInterceptor = jwtInterceptor;
		this.userEmailResolver = userEmailResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
			.addPathPatterns(DELIVERY_ALL)
			.excludePathPatterns(AUTH_ALL);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userEmailResolver);
	}
}
