package br.com.cursoudemy.productapi.api.modules.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
	
	@NotBlank
	private String name;
	
	@NotNull
	@PositiveOrZero
	private Integer quantityAvailable;
	
	@NotNull
	private Integer categoryId;
	
	@NotNull
	private Integer supplierId;
	
}
