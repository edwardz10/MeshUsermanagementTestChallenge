package com.mesh.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

@Data
public class Phone {
    private String id;
    private String value;
}
