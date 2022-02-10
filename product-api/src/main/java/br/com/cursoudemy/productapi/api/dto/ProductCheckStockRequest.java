package br.com.cursoudemy.productapi.api.dto;

import java.util.List;

import br.com.cursoudemy.productapi.domain.listener.dto.ProductQuantityDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCheckStockRequest {

	List<ProductQuantityDTO> products;
	
}
