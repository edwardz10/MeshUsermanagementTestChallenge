package com.mesh.usermanagement.controller;

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
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
		log.error(ex.getMessage());
		return ResponseEntity.status(SC_BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
				.body(ex.getMessage());
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	protected ResponseEntity<Object> handlePersistenceException(final DataIntegrityViolationException ex) {
		val errorMessage = ex.getRootCause() == null
				? ex.getMessage()
				: ex.getRootCause().getMessage();
		return ResponseEntity.status(SC_BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
				.body(errorMessage);
	}

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<String> handleGenericException(Exception ex) {
		log.error(ex.getMessage());
		return ResponseEntity.status(SC_INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(ex.getMessage());
	}
}
