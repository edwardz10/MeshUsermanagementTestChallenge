package com.mesh.usermanagement.service.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Value("${application.security.username}")
	private String securedUsername;
	@Value("${application.security.password}")
	private String securedPassword;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (securedUsername.equals(username)) {
			return new User(securedUsername, new BCryptPasswordEncoder().encode(securedPassword),
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}