package com.mesh.usermanagement.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiErrorResponse {

	private Integer code;
	private String errorMesage;
}
