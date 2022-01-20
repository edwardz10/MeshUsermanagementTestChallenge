package com.mesh.usermanagement.service;

import com.mesh.usermanagement.entity.ProfileEntity;
import com.mesh.usermanagement.entity.UserEntity;
import com.mesh.usermanagement.exception.UserManagementException;
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
import java.util.HashSet;

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
  public User updateUser(String userId, User user) throws UserManagementException {
    UserEntity userEntity = userRepository.findByExternalId(userId);

    if (userEntity == null) {
      log.info("User with id {} not found", userId);
      return null;
    } else {
      if (!userEntity.getName().equals(user.getName())) {
        throw new UserManagementException("User name does not match");
      }

      if (!userEntity.getAge().equals(user.getAge())) {
        throw new UserManagementException("User age does not match");
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
