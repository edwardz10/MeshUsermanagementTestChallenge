package com.mesh.usermanagement.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mesh.usermanagement.exception.UserManagementNotAuthorizedException;
import com.mesh.usermanagement.model.ApiErrorResponse;
import com.mesh.usermanagement.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");

		if (requestTokenHeader == null || (!requestTokenHeader.startsWith("Bearer ")
			&& !requestTokenHeader.startsWith("bearer"))) {
			response.setStatus(SC_UNAUTHORIZED);
			response.getWriter().write(objectMapper.writeValueAsString(unauthorizedApiError()));
			response.flushBuffer();
			return;
		}

		String username = null;
		String jwtToken = requestTokenHeader.split(" ")[1];

		try {
			try {
				username = jwtTokenService.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				throw new UserManagementNotAuthorizedException("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				throw new UserManagementNotAuthorizedException("JWT Token has expired");
			} catch (MalformedJwtException e) {
				throw new UserManagementNotAuthorizedException(e.getMessage());
			}

			// Once we get the token validate it.
			if (StringUtils.isEmpty(username)) {
				throw new UserManagementNotAuthorizedException("Username not found");
			}

			chain.doFilter(request, response);
		} catch (UserManagementNotAuthorizedException e) {
			response.setStatus(SC_FORBIDDEN);
			response.getWriter().write(objectMapper.writeValueAsString(forbiddenApiError(e.getErrorMessage())));
			response.flushBuffer();
		}
	}

	private ApiErrorResponse unauthorizedApiError() {
		return ApiErrorResponse.builder()
				.code(SC_UNAUTHORIZED)
				.errorMesage("Bearer token not found")
				.build();
	}

	private ApiErrorResponse forbiddenApiError(String errorMessage) {
		return ApiErrorResponse.builder()
				.code(SC_FORBIDDEN)
				.errorMesage(errorMessage)
				.build();
	}
}
