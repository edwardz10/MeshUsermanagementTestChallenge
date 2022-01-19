package com.mesh.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class User {
    private String id;
    @NotNull
    private String name;
    @NotNull
    @Min(1)
    private Integer age;
    @NotNull
    @Email
    private String email;
    private Set<Phone> phones;
    @NotNull
    private Profile profile;
}
