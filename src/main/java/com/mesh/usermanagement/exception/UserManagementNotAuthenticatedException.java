package com.mesh.usermanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserManagementNotAuthenticatedException extends RuntimeException {

	private String errorMessage;

}
