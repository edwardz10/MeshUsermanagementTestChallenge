package com.mesh.usermanagement.config;

import com.mesh.usermanagement.repository.ProfileRepository;
import com.mesh.usermanagement.service.CacheIncrementService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableScheduling
public class AppConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@ConditionalOnProperty(prefix = "cacheIncrement", value = "enabled")
	public CacheIncrementService cacheIncrementService(ProfileRepository profileRepository) {
		return new CacheIncrementService(profileRepository);
	}
}
