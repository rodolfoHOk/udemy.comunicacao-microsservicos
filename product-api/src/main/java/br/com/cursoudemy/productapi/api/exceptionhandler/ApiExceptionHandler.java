package br.com.cursoudemy.productapi.api.exceptionhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.cursoudemy.productapi.domain.exception.BusinessException;
import br.com.cursoudemy.productapi.domain.exception.ClientRequestException;
import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		
		var problemDetails = new ProblemDetails(status.value(), ex.getMessage());
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		var status = HttpStatus.NOT_FOUND;
		
		var problemDetails = new ProblemDetails(status.value(), ex.getMessage());
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ClientRequestException.class)
	public ResponseEntity<?> handleClientRequestException(ClientRequestException ex, WebRequest request) {
		var status = HttpStatus.BAD_GATEWAY;
		
		var problemDetails = new ProblemDetails(status.value(), ex.getMessage());
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBusinessException(BusinessException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		
		var problemDetails = new ProblemDetails(status.value(), ex.getMessage());
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleUncaught(Exception ex, WebRequest request) {
		var status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		var problemDetails = new ProblemDetails(status.value(), "An unexpected internal system error has occurred");
		
		log.error(ex.getMessage(), ex);
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (body == null) {
			body = new ProblemDetails(status.value(), ex.getMessage());
		} else if (body instanceof String) {
			body = new ProblemDetails(status.value(), (String) body);
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
}
