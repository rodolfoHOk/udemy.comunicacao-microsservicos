package br.com.cursoudemy.productapi.api.modules.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
	
	private String name;
	
	private Integer quantityAvailable;
	
	private Integer categoryId;
	
	private Integer supplierId;
	
}
