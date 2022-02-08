package br.com.cursoudemy.productapi.api.assembler;

import java.util.List;

import br.com.cursoudemy.productapi.api.dto.ProductResponse;
import br.com.cursoudemy.productapi.domain.model.Product;

public class ProductResponseAssembler {
	
	public static ProductResponse toModel (Product product) {
		return ProductResponse
				.builder()
				.id(product.getId())
				.name(product.getName())
				.quantityAvailable(product.getQuantityAvailable())
				.createdAt(product.getCreatedAt())
				.category(CategoryResponseAssembler.toModel(product.getCategory()))
				.supplier(SupplierResponseAssembler.toModel(product.getSupplier()))
				.build();
	}
	
	public static List<ProductResponse> toCollectionModel (List<Product> products) {
		return products.stream().map(product -> toModel(product)).toList();
	}
	
}
