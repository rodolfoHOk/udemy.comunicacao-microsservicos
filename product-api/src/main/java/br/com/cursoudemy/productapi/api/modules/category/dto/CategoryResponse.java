package br.com.cursoudemy.productapi.api.modules.category.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {

	@ApiModelProperty(example = "1")
	private Integer id;
	
	@ApiModelProperty(example = "Books")
	private String description;
	
}
