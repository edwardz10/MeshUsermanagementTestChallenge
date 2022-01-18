package com.mesh.usermanagement.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="profiles")
@Data
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cash;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

}
