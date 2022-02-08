package br.com.cursoudemy.productapi.domain.exception;

public class ValidationException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super(message);
	}
	
}
