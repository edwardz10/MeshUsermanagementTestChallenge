package com.mesh.usermanagement.service;

import com.mesh.usermanagement.UsermanagementApplication;
import com.mesh.usermanagement.entity.ProfileEntity;
import com.mesh.usermanagement.entity.UserEntity;
import com.mesh.usermanagement.repository.UserRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UsermanagementApplication.class)
class UserServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void getAllUsersTest() {
		val usersIterable = userRepository.findAll();
		List<UserEntity> userList = new ArrayList<>();

		usersIterable.iterator().forEachRemaining(userList::add);
		assertFalse(userList.isEmpty());
	}

	@Test
	public void createUserTest() {
		var usersIterable = userRepository.findAll();
		List<UserEntity> userList = new ArrayList<>();

		usersIterable.iterator().forEachRemaining(userList::add);
		int userCount = userList.size();

		val newUserEntity = new UserEntity();
		newUserEntity.setName("Name");
		newUserEntity.setEmail("email@email.com");
		newUserEntity.setAge(11);
		val newProfile = new ProfileEntity();
		newProfile.setCash(12000);
		newProfile.setUser(newUserEntity);
		newUserEntity.setProfile(newProfile);

		userRepository.save(newUserEntity);
		usersIterable = userRepository.findAll();
		userList.clear();

		usersIterable.iterator().forEachRemaining(userList::add);
		assertEquals(userList.size(), userCount + 1);
	}

	@Test
	public void createUserAndGetItByIdTest() {
		var newUserEntity = new UserEntity();
		newUserEntity.setName("Name2");
		newUserEntity.setEmail("email2@email.com");
		newUserEntity.setAge(11);
		val newProfile = new ProfileEntity();
		newProfile.setCash(12000);
		newProfile.setUser(newUserEntity);
		newUserEntity.setProfile(newProfile);

		newUserEntity = userRepository.save(newUserEntity);

		val retrievedUserEntity = userRepository.findByExternalId(newUserEntity.getExternalId());
		assertNotNull(retrievedUserEntity);
		assertEquals(retrievedUserEntity.getEmail(), newUserEntity.getEmail());
	}
}