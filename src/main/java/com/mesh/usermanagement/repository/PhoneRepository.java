package com.mesh.usermanagement.repository;

import com.mesh.usermanagement.entity.PhoneEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends CrudRepository<PhoneEntity, Long> {
}
