package br.com.cursoudemy.productapi.api.modules.product.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.cursoudemy.productapi.domain.modules.product.listener.dto.ProductQuantityDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCheckStockRequest {

	@Valid
	@Size(min = 1)
	@NotNull
	List<ProductQuantityDTO> products;
	
}
