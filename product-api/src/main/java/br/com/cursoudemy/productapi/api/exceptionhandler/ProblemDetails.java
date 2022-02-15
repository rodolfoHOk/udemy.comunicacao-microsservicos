package br.com.cursoudemy.productapi.api.exceptionhandler;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ProblemDetails {
	
	@ApiModelProperty(example = "400")
	private Integer status;
	
	@ApiModelProperty(example = "Invalid Request (client error)")
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
		
		@ApiModelProperty(example = "description")
		private String name;
		
		@ApiModelProperty(example = "must not be blank")
		private String message;
	}
	
}
