package com.mesh.usermanagement.service;

import com.mesh.usermanagement.entity.PhoneEntity;
import com.mesh.usermanagement.entity.ProfileEntity;
import com.mesh.usermanagement.entity.UserEntity;
import com.mesh.usermanagement.model.User;
import com.mesh.usermanagement.repository.PhoneRepository;
import com.mesh.usermanagement.repository.ProfileRepository;
import com.mesh.usermanagement.repository.UserRepository;
import com.mesh.usermanagement.util.ConversionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PhoneRepository phoneRepository;
  private final ProfileRepository profileRepository;

  @Transactional
  public User createUser(User user) {
    UserEntity userEntity = userRepository.save(ConversionUtils.convert(user));
    ProfileEntity profileEntity = null;
    Set<PhoneEntity> phoneEntitiesSet = new HashSet<>();

    if (user.getProfile() != null) {
      profileEntity = ConversionUtils.convert(user.getProfile());
      profileEntity.setUser(userEntity);
      profileEntity = profileRepository.save(profileEntity);
    }

    if (CollectionUtils.isNotEmpty(user.getPhones())) {
      user.getPhones().forEach(phone -> {
        PhoneEntity phoneEntity = ConversionUtils.convert(phone);
        phoneEntity = phoneRepository.save(phoneEntity);
        phoneEntitiesSet.add(phoneEntity);
      });
    }

    User resultUser = ConversionUtils.convert(userEntity);

    if (profileEntity != null) {
      resultUser.setProfile(ConversionUtils.convert(profileEntity));
    }

    if (CollectionUtils.isNotEmpty(phoneEntitiesSet)) {
      resultUser.setPhones(phoneEntitiesSet.stream().map(ConversionUtils::convert).collect(Collectors.toSet()));
    }

    return resultUser;
  }

}
