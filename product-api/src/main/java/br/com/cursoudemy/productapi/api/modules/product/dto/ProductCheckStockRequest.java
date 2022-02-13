package br.com.cursoudemy.productapi.api.modules.product.dto;

import java.util.List;

import br.com.cursoudemy.productapi.domain.modules.product.listener.dto.ProductQuantityDTO;
import lombok.Data;

@Data
public class ProductCheckStockRequest {

	List<ProductQuantityDTO> products;
	
}
