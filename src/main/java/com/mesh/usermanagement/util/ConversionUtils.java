package com.mesh.usermanagement.util;

import com.mesh.usermanagement.entity.PhoneEntity;
import com.mesh.usermanagement.entity.ProfileEntity;
import com.mesh.usermanagement.entity.UserEntity;
import com.mesh.usermanagement.model.Phone;
import com.mesh.usermanagement.model.Profile;
import com.mesh.usermanagement.model.User;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;

import java.util.stream.Collectors;

public class ConversionUtils {

  public static User convert(UserEntity userEntity) {
    val user = new User();
    user.setId(userEntity.getId());
    user.setName(userEntity.getName());
    user.setAge(userEntity.getAge());
    user.setEmail(userEntity.getEmail());

    if (userEntity.getProfile() != null) {
      user.setProfile(convert(userEntity.getProfile()));
    }

    if (CollectionUtils.isNotEmpty(userEntity.getPhones())) {
      user.setPhones(userEntity.getPhones().stream()
          .map(ConversionUtils::convert).collect(Collectors.toSet()));
    }

    return user;
  }

  public static Phone convert(PhoneEntity phoneEntity) {
    val phone = new Phone();
    phone.setId(phoneEntity.getId());
    phone.setValue(phoneEntity.getValue());
    return phone;
  }

  public static Profile convert(ProfileEntity profileEntity) {
    val profile = new Profile();
    profile.setId(profileEntity.getId());
    profile.setKopeks(profileEntity.getCash() % 100);
    profile.setRoubles(profileEntity.getCash() / 100);
    return profile;
  }

  public static UserEntity convert(User user) {
    val userEntity = new UserEntity();
    userEntity.setId(user.getId());
    userEntity.setName(user.getName());
    userEntity.setAge(user.getAge());
    userEntity.setEmail(user.getEmail());

    if (user.getProfile() != null) {
      userEntity.setProfile(convert(user.getProfile()));
    }

    if (CollectionUtils.isNotEmpty(user.getPhones())) {
      userEntity.setPhones(user.getPhones().stream().map(ConversionUtils::convert).collect(Collectors.toSet()));
    }

    return userEntity;
  }

  public static PhoneEntity convert(Phone phone) {
    val phoneEntity = new PhoneEntity();
    phoneEntity.setId(phone.getId());
    phoneEntity.setValue(phone.getValue());
    return phoneEntity;
  }

  public static ProfileEntity convert(Profile profile) {
    val profileEntity = new ProfileEntity();
    profileEntity.setId(profile.getId());
    profileEntity.setCash(profile.getKopeks() + profile.getRoubles()*100);
    return profileEntity;
  }
}
