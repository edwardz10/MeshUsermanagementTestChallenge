package com.mesh.usermanagement.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FilterParameters {
	private String name;
	private Integer age;
	private String email;
	private String phone;
}
