package com.mesh.usermanagement.controller;

import com.mesh.usermanagement.model.jwt.JwtRequest;
import com.mesh.usermanagement.model.jwt.JwtResponse;
import com.mesh.usermanagement.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtAuthenticationController {

	private final JwtTokenService jwtTokenService;

	@PostMapping("/authenticate")
	public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest jwtRequest) {
		final String token = jwtTokenService.generateToken(jwtRequest.getUsername());
		return ResponseEntity.ok(new JwtResponse(token));
	}
}
