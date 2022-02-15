package br.com.cursoudemy.productapi.api.modules.category.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

	@NotBlank
	private String description;
	
}
