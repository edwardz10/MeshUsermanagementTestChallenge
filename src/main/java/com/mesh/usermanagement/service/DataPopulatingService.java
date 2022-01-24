package com.mesh.usermanagement.service;

import com.mesh.usermanagement.model.Phone;
import com.mesh.usermanagement.model.Profile;
import com.mesh.usermanagement.model.User;
import com.mesh.usermanagement.repository.UserRepository;
import com.mesh.usermanagement.util.ConversionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataPopulatingService {

	private final UserRepository userRepository;

	@PostConstruct
	public void createUsers() {
		createUser("Vladimir", 22, "vladimir@gmail.com", 81, 00, Set.of("12313424"));
		createUser("Olga", 48, "olga@gmail.com", 200, 00, Set.of("43243444"));
		createUser("Mikhail", 22, "mikhail@gmail.com", 180, 13, Set.of("5453455"));
		createUser("Egor", 19, "egor@gmail.com", 80, 00, Set.of("55345345"));
		createUser("Svetlana", 33, "svetlana@gmail.com", 200, 00, Set.of("65655"));
		createUser("Irina", 17, "irina@gmail.com", 160, 00, Set.of("45345"));
		createUser("Eugeny", 19, "eugeny@gmail.com", 30, 00, Set.of("656565"));
		createUser("Timofei", 39, "timofei@gmail.com", 33, 00, Set.of("66655554"));
		createUser("Daniil", 38, "daniil@gmail.com", 67, 00, Set.of("88777"));
	}

	private void createUser(String name, int age, String email, int roubles, int kopeks, Set<String> phoneNumbers) {
		val user = new User();
		user.setName(name);
		user.setAge(age);
		user.setEmail(email);
		user.setPhones(new HashSet<>());

		val profile = new Profile();
		profile.setRoubles(roubles);
		profile.setKopeks(kopeks);
		user.setProfile(profile);

		phoneNumbers.forEach(phoneNumber -> {
			val phone = new Phone();
			phone.setValue(phoneNumber);
			user.getPhones().add(phone);
		});

		val userEntity = ConversionUtils.convert(user);

		try {
			userRepository.save(userEntity);
		} catch (Exception e) {
			log.warn("Failed to create user entity {}: {}", userEntity, e.getMessage());
		}
	}
}
