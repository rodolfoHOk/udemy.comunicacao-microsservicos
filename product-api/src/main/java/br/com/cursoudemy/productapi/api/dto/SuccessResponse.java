package br.com.cursoudemy.productapi.api.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {

	private Integer status;
	
	private String message;
	
	public static SuccessResponse create (String message) {
		return new SuccessResponse(HttpStatus.OK.value(), message);
	}
	
}
