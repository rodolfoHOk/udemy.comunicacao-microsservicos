package br.com.cursoudemy.productapi.api.exceptionhandler;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ProblemDetails {
	
	private Integer status;
	
	private String message;
	
	private List<FieldsError> fieldsError;
	
	public ProblemDetails (Integer status, String message) {
		this.status = status;
		this.message = message;
	}
 	
	@Getter
	@Setter
	@AllArgsConstructor
	public class FieldsError {
		
		private String name;
		
		private String message;
	}
	
}
