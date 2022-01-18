package com.mesh.usermanagement.repository;

import com.mesh.usermanagement.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Long> {
}
