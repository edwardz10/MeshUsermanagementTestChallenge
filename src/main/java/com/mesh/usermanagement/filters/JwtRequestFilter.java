package com.mesh.usermanagement.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mesh.usermanagement.exception.UserManagementNotAuthorizedException;
import com.mesh.usermanagement.model.ApiErrorResponse;
import com.mesh.usermanagement.service.jwt.JwtTokenService;
import com.mesh.usermanagement.service.jwt.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
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

	private final RequestMatcher authenticatorRequestMatcher = new AntPathRequestMatcher("/authenticate");
	private final RequestMatcher swaggerRequestMatcher1 = new AntPathRequestMatcher( "/swagger-ui*");
	private final RequestMatcher swaggerRequestMatcher2 = new AntPathRequestMatcher( "/v2/api-docs");
	private final RequestMatcher swaggerRequestMatcher3 = new AntPathRequestMatcher( "/webjars/**");

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		if (this.authenticatorRequestMatcher.matches(request)
			|| this.swaggerRequestMatcher1.matches(request)
			|| this.swaggerRequestMatcher2.matches(request)
			|| this.swaggerRequestMatcher3.matches(request)) {
			chain.doFilter(request, response);
		} else {
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

				//Once we get the token validate it.
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

					// if token is valid configure Spring Security to manually set authentication
					if (jwtTokenService.validateToken(jwtToken, userDetails)) {

						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				}

				chain.doFilter(request, response);
			} catch (UserManagementNotAuthorizedException e) {
				response.setStatus(SC_FORBIDDEN);
				response.getWriter().write(objectMapper.writeValueAsString(forbiddenApiError(e.getErrorMessage())));
				response.flushBuffer();
			}
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
