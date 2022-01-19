package com.mesh.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {
    private String id;
    @NotNull
    private Integer roubles;
    @NotNull
    private Integer kopeks;
}
