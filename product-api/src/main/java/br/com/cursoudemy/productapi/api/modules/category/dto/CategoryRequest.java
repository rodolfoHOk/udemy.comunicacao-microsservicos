package br.com.cursoudemy.productapi.api.modules.category.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

	@ApiModelProperty(example = "Books")
	@NotBlank
	private String description;
	
}
