package com.mesh.usermanagement.util;

import com.mesh.usermanagement.entity.PhoneEntity;
import com.mesh.usermanagement.entity.ProfileEntity;
import com.mesh.usermanagement.entity.UserEntity;
import com.mesh.usermanagement.model.Phone;
import com.mesh.usermanagement.model.Profile;
import com.mesh.usermanagement.model.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class ConversionUtils {

  public static User convert(UserEntity userEntity) {
    val user = new User();
    user.setId(userEntity.getExternalId());
    user.setName(userEntity.getName());
    user.setAge(userEntity.getAge());
    user.setEmail(userEntity.getEmail());

    if (userEntity.getProfile() != null) {
      user.setProfile(convert(userEntity.getProfile()));
    }

    if (CollectionUtils.isNotEmpty(userEntity.getPhones())) {
      user.setPhones(userEntity.getPhones().stream().map(ConversionUtils::convert).collect(Collectors.toSet()));
    }

    return user;
  }

  public static Phone convert(PhoneEntity phoneEntity) {
    val phone = new Phone();
    phone.setId(phoneEntity.getExternalId());
    phone.setValue(phoneEntity.getValue());
    return phone;
  }

  public static Profile convert(ProfileEntity profileEntity) {
    val profile = new Profile();
    profile.setId(profileEntity.getExternalId());
    profile.setKopeks(profileEntity.getCash() % 100);
    profile.setRoubles(profileEntity.getCash() / 100);
    return profile;
  }

  public static UserEntity convert(User user) {
    val userEntity = new UserEntity();
    userEntity.setExternalId(user.getId() == null
        ? UUID.randomUUID().toString()
        : user.getId());
    userEntity.setName(user.getName());
    userEntity.setAge(user.getAge());
    userEntity.setEmail(user.getEmail());

    if (user.getProfile() != null) {
      val profileEntity = convert(user.getProfile());
      profileEntity.setUser(userEntity);
      userEntity.setProfile(profileEntity);
    }

    if (CollectionUtils.isNotEmpty(user.getPhones())) {
      for (val phone : user.getPhones()) {
        val phoneEntity = convert(phone);
        phoneEntity.setUser(userEntity);
        userEntity.addPhone(phoneEntity);
      }
    }

    return userEntity;
  }

  public static PhoneEntity convert(Phone phone) {
    val phoneEntity = new PhoneEntity();
    phoneEntity.setExternalId(phone.getId() == null
        ? UUID.randomUUID().toString()
        : phone.getId());
    phoneEntity.setValue(phone.getValue());
    return phoneEntity;
  }

  public static ProfileEntity convert(Profile profile) {
    val profileEntity = new ProfileEntity();
    profileEntity.setExternalId(profile.getId() == null
        ? UUID.randomUUID().toString()
        : profile.getId());
    profileEntity.setCash(profile.getKopeks() + profile.getRoubles()*100);
    return profileEntity;
  }

  public static void updatePhones(User user, UserEntity userEntity) {
    for (val phone : user.getPhones()) {
      for (val phoneEntity : userEntity.getPhones()) {
        if (phone.getId() != null) {
          if (phone.getId().equals(phoneEntity.getExternalId())) {
            if (phone.getValue().equals(phoneEntity.getValue())) {
              log.info("Phone {} ({}) stays the same", phone.getId(), phone.getValue());
            } else {
              log.info("Phone {} is updated; new number is {}", phone.getId(), phone.getValue());
              phoneEntity.setValue(phone.getValue());
            }
          }
        } else {
          log.info("New phone: ({})", phone.getValue());
          val newPhoneEntity = convert(phone);
          newPhoneEntity.setUser(userEntity);
          userEntity.addPhone(newPhoneEntity);
        }
      }
    }
  }
}
