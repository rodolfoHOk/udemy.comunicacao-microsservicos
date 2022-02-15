package br.com.cursoudemy.productapi.domain.modules.product.listener.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDTO {
	
	@NotNull
	private Integer productId;
	
	@NotNull
	@Positive
	private Integer quantity;
	
}
