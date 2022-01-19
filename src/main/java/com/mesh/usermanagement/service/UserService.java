package com.mesh.usermanagement.service;

import com.mesh.usermanagement.entity.UserEntity;
import com.mesh.usermanagement.model.User;
import com.mesh.usermanagement.repository.PhoneRepository;
import com.mesh.usermanagement.repository.ProfileRepository;
import com.mesh.usermanagement.repository.UserRepository;
import com.mesh.usermanagement.util.ConversionUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
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

  public User getUser(String userId) {
    val userEntity = userRepository.findByExternalId(userId);
    return ConversionUtils.convert(userEntity);
  }
}
