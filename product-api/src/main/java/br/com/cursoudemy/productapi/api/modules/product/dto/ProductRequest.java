package br.com.cursoudemy.productapi.api.modules.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
	
	@ApiModelProperty(example = "The Secret")
	@NotBlank
	private String name;
	
	@ApiModelProperty(example = "10")
	@NotNull
	@PositiveOrZero
	private Integer quantityAvailable;
	
	@ApiModelProperty(example = "1")
	@NotNull
	private Integer categoryId;
	
	@ApiModelProperty(example = "2")
	@NotNull
	private Integer supplierId;
	
}
