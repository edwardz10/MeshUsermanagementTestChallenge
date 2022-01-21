package com.mesh.usermanagement.exception;

import com.mesh.usermanagement.model.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static javax.servlet.http.HttpServletResponse.*;

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

	@ExceptionHandler(value = {UserManagementServiceException.class})
	public ResponseEntity<ApiErrorResponse> handleServiceException(UserManagementServiceException ex) {
		log.error(ex.getMessage());
		ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
				.code(SC_BAD_REQUEST)
				.errorMesage(ex.getErrorMessage())
				.build();

		return getResponseEntity(apiErrorResponse);
	}

	@ExceptionHandler(value = {UserManagementNotAuthorizedException.class})
	public ResponseEntity<ApiErrorResponse> handleNotAuthorizedException(UserManagementNotAuthorizedException ex) {
		log.error(ex.getMessage());
		ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
				.code(SC_UNAUTHORIZED)
				.errorMesage(ex.getErrorMessage())
				.build();

		return getResponseEntity(apiErrorResponse);
	}

	@ExceptionHandler(value = {UserManagementNotAuthenticatedException.class})
	public ResponseEntity<ApiErrorResponse> handleNotAuthenticatdException(UserManagementNotAuthenticatedException ex) {
		log.error(ex.getMessage());
		ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
				.code(SC_FORBIDDEN)
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
