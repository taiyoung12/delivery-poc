package com.barogo.java.delivery.poc.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.barogo.java.delivery.poc.common.config.token.JwtValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
	private final JwtValidator jwtValidator;

	public JwtInterceptor(JwtValidator jwtValidator) {
		this.jwtValidator = jwtValidator;
	}

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) throws Exception {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			if (jwtValidator.validate(token)) {
				String userId = jwtValidator.extractSubject(token);
				request.setAttribute("userId", userId);
				return true;
			}
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write("Unauthorized");
		return false;
	}
}
