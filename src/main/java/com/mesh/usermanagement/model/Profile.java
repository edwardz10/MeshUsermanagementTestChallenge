package com.mesh.usermanagement.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Profile {
    private Long id;
    @NotNull
    private Integer roubles;
    @NotNull
    private Integer kopeks;
    private Long userId;
}
