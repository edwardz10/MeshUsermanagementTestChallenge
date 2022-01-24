package com.mesh.usermanagement.controller;

import com.mesh.usermanagement.model.jwt.JwtRequest;
import com.mesh.usermanagement.model.jwt.JwtResponse;
import com.mesh.usermanagement.service.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationController {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenService jwtTokenService;

	private final UserDetailsService jwtInMemoryUserDetailsService;

	@PostMapping("/authenticate")
	public ResponseEntity<JwtResponse> createToken(@RequestBody JwtRequest jwtRequest) {
		log.info("Request to get an acess token: {}", jwtRequest);
		authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(jwtRequest.getUsername());

		final String token = jwtTokenService.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws UsernameNotFoundException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new UsernameNotFoundException("User disabled", e);
		} catch (BadCredentialsException e) {
			throw new UsernameNotFoundException("Invalid credentials", e);
		}
	}
}
