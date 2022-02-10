package br.com.cursoudemy.productapi.domain.exception;

public class ClientRequestException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public ClientRequestException(String message) {
		super(message);
	}
	
}
