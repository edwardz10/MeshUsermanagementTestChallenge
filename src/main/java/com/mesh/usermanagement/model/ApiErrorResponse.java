package com.mesh.usermanagement.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiErrorResponse {

	private Integer code;
	private String errorMesage;
}
