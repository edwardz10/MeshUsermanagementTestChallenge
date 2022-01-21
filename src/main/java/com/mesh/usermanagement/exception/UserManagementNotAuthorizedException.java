package com.mesh.usermanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserManagementNotAuthorizedException extends RuntimeException {

	private String errorMessage;

}
