package com.mesh.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="users")
@Getter
@Setter
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String externalId;
    private String name;
    private Integer age;
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhoneEntity> phones = new HashSet<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    private ProfileEntity profile;

    public void addPhone(PhoneEntity phoneEntity) {
        phones.add(phoneEntity);
    }

    public Set<String> getPhoneNumbersSet() {
        return phones.stream().map(PhoneEntity::getValue).collect(Collectors.toSet());
    }
}
