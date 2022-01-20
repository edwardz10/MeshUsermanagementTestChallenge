package com.mesh.usermanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UserManagementException extends Exception {

	private String errorMessage;

}
