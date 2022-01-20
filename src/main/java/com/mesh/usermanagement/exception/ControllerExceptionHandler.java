package com.mesh.usermanagement.exception;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
		log.error(ex.getMessage());
		ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
				.code(SC_BAD_REQUEST)
				.errorMesage(ex.getMessage())
				.build();

		return getResponseEntity(apiErrorResponse);
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	protected ResponseEntity<ApiErrorResponse> handlePersistenceException(final DataIntegrityViolationException ex) {
		log.error(ex.getMessage());
		ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
				.code(SC_BAD_REQUEST)
				.errorMesage(ex.getRootCause() == null
						? ex.getMessage()
						: ex.getRootCause().getMessage())
				.build();

		return getResponseEntity(apiErrorResponse);
	}

	@ExceptionHandler(value = {UserManagementException.class})
	public ResponseEntity<ApiErrorResponse> handleGenericException(UserManagementException ex) {
		log.error(ex.getMessage());
		ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
				.code(SC_BAD_REQUEST)
				.errorMesage(ex.getErrorMessage())
				.build();

		return getResponseEntity(apiErrorResponse);
	}

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
		log.error(ex.getMessage());
		ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
				.code(SC_INTERNAL_SERVER_ERROR)
				.errorMesage(ex.getMessage())
				.build();

		return getResponseEntity(apiErrorResponse);
	}

	private ResponseEntity<ApiErrorResponse> getResponseEntity(ApiErrorResponse apiErrorResponse) {
		return ResponseEntity.status(apiErrorResponse.getCode()).contentType(MediaType.APPLICATION_JSON)
				.body(apiErrorResponse);
	}
}
