package com.mesh.usermanagement.service;

import com.mesh.usermanagement.entity.ProfileEntity;
import com.mesh.usermanagement.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CacheIncrementService {

	private final ProfileRepository profileRepository;

	@Scheduled(fixedRateString = "${application.increment.delay}")
	public void incrementProfiles() {
		log.info("Start of the increment prefixes' task");

		Iterable<ProfileEntity> profileEntities = profileRepository.findAll();
		profileEntities.forEach(this::incrementProfileEntity);

		log.info("End of the increment prefixes' task");
	}

	private void incrementProfileEntity(ProfileEntity profileEntity) {
		if (profileEntity.getCash() * 1.1 <= profileEntity.getMaxCash()) {
			profileEntity.setCash((int) (profileEntity.getCash() * 1.1));
			profileRepository.save(profileEntity);
			log.info("Cache of profile entity {} has been incremented", profileEntity.getExternalId());
		} else {
			log.info("Cache of profile entity {} has reached its limit and cannot be incremented", profileEntity.getExternalId());
		}
	}
}
