package br.com.cursoudemy.productapi.api.exceptionhandler;

import java.util.List;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.cursoudemy.productapi.domain.exception.BusinessException;
import br.com.cursoudemy.productapi.domain.exception.ClientRequestException;
import br.com.cursoudemy.productapi.domain.exception.EntityInUseException;
import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidation(ValidationException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		
		var problemDetails = new ProblemDetails(status.value(), ex.getMessage());
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(EntityInUseException.class)
	public ResponseEntity<?> EntityInUse(EntityInUseException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		
		var problemDetails = new ProblemDetails(status.value(), ex.getMessage());
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
		var status = HttpStatus.NOT_FOUND;
		
		var problemDetails = new ProblemDetails(status.value(), ex.getMessage());
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ClientRequestException.class)
	public ResponseEntity<?> handleClientRequest(ClientRequestException ex, WebRequest request) {
		var status = HttpStatus.BAD_GATEWAY;
		
		var problemDetails = new ProblemDetails(status.value(), ex.getMessage());
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBusiness(BusinessException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		
		var problemDetails = new ProblemDetails(status.value(), ex.getMessage());
		
		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
		var status = HttpStatus.FORBIDDEN;
		
		var problemDetails = new ProblemDetails(status.value(), "You do not have permission to perform this operation");
		
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
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		var problemDetails = new ProblemDetails(status.value(), "Validation error");
		
		List<ProblemDetails.FieldsError> fieldsError = ex.getBindingResult().getAllErrors().stream()
				.map(objectError -> {
					String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
					String name = objectError.getObjectName();
					if (objectError instanceof FieldError) {
						name = ((FieldError) objectError).getField();
					}
					return problemDetails.new FieldsError(name, message);
				}).toList();
		
		problemDetails.setFieldsError(fieldsError);

		return handleExceptionInternal(ex, problemDetails, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (ex instanceof MethodArgumentTypeMismatchException) {
			var problemDetails = new ProblemDetails(status.value(), "The url parameter of an invalid type");
			return handleExceptionInternal(ex, problemDetails, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ProblemDetails problemDetails;
		if (ex.getMessage().startsWith("Required request body is missing")) {
			problemDetails = new ProblemDetails(status.value(), "Required request body is missing");
		} else if (ex.getMessage().startsWith("JSON parse error: Unrecognized field")) {
			problemDetails = new ProblemDetails(status.value(), "Request body contains unknown properties");
		} else {
			problemDetails = new ProblemDetails(status.value(), "The request body is invalid. Check syntax error");
		}
		
		return handleExceptionInternal(ex, problemDetails, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		var problemDetails = new ProblemDetails(status.value(), "The resource you tried to access does not exist");
		
		return handleExceptionInternal(ex, problemDetails, headers, status, request);
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
