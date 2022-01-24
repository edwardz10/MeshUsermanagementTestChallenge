package com.mesh.usermanagement.service;

import com.mesh.usermanagement.entity.ProfileEntity;
import com.mesh.usermanagement.entity.UserEntity;
import com.mesh.usermanagement.exception.UserManagementServiceException;
import com.mesh.usermanagement.model.FilterParameters;
import com.mesh.usermanagement.model.User;
import com.mesh.usermanagement.repository.PhoneRepository;
import com.mesh.usermanagement.repository.ProfileRepository;
import com.mesh.usermanagement.repository.UserRepository;
import com.mesh.usermanagement.util.ConversionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PhoneRepository phoneRepository;
  private final ProfileRepository profileRepository;

  @Transactional
  public User createUser(User user) {
    UserEntity userEntity = ConversionUtils.convert(user);
    userEntity = userRepository.save(userEntity);
    return ConversionUtils.convert(userEntity);
  }

  @Transactional
  public User updateUser(String userId, User user) throws UserManagementServiceException {
    UserEntity userEntity = userRepository.findByExternalId(userId);

    if (userEntity == null) {
      log.info("User with id {} not found", userId);
      return null;
    } else {
      if (!userEntity.getName().equals(user.getName())) {
        throw new UserManagementServiceException("User name does not match");
      }

      if (!userEntity.getAge().equals(user.getAge())) {
        throw new UserManagementServiceException("User age does not match");
      }

      /**
       * Update email in the user entity.
       */
      userEntity.setEmail(user.getEmail());

      /**
       * Update phones in the user entity.
       */
      ConversionUtils.updatePhones(user, userEntity);


      /**
       * Update profile in the user entity
       */
      Long profileId = userEntity.getProfile().getId();
      ProfileEntity profileEntity = ConversionUtils.convert(user.getProfile());
      profileEntity.setId(profileId);
      profileEntity.setUser(userEntity);
      userEntity.setProfile(profileEntity);

      userEntity = userRepository.save(userEntity);

      return ConversionUtils.convert(userEntity);
    }
  }

  public List<User> getAllUsers(FilterParameters filterParameters) {
    log.info("Get all users with filtering parameters: {}", filterParameters);
    List<User> userList = new ArrayList<>();
    val usersIterable = userRepository.findAll();

    usersIterable.forEach(userEntity -> {
      boolean isOk = true;

      if (filterParameters.getName() != null && !userEntity.getName().contains(filterParameters.getName())) {
        isOk = false;
      }

      if (isOk && filterParameters.getEmail() != null && !userEntity.getEmail().equals(filterParameters.getEmail())) {
        isOk = false;
      }

      if (isOk && filterParameters.getAge() != null && !Objects.equals(userEntity.getAge(), filterParameters.getAge())) {
        isOk = false;
      }

      if (isOk && filterParameters.getPhone() != null && !userEntity.getPhoneNumbersSet().contains(filterParameters.getPhone())) {
        isOk = false;
      }

      if (isOk) {
        userList.add(ConversionUtils.convert(userEntity));
      }
    });
    return userList;
  }

  public User getUser(String userId) {
    val userEntity = userRepository.findByExternalId(userId);

    if (userEntity == null) {
      log.info("User with id {} not found", userId);
      return null;
    }

    return ConversionUtils.convert(userEntity);
  }

  public void deleteUser(String userId) {
    val userEntity = userRepository.findByExternalId(userId);

    if (userEntity != null) {
      userRepository.delete(userEntity);
    }
  }

}
