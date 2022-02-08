package br.com.cursoudemy.productapi.api.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProblemDetails {
	
	private Integer status;
	
	private String message;
	
}
