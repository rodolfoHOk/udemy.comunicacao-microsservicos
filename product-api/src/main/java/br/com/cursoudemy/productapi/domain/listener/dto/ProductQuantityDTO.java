package br.com.cursoudemy.productapi.domain.listener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDTO {
	
	private Integer productId;
	
	private Integer quantity;
	
}