package com.mesh.usermanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserManagementServiceException extends Exception {

	private String errorMessage;

}
