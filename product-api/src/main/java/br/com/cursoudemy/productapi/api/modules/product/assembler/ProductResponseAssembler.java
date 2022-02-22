package br.com.cursoudemy.productapi.api.modules.product.assembler;

import java.util.List;
import java.util.stream.Collectors;

import br.com.cursoudemy.productapi.api.modules.category.assembler.CategoryResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductResponse;
import br.com.cursoudemy.productapi.api.modules.supplier.assembler.SupplierResponseAssembler;
import br.com.cursoudemy.productapi.domain.modules.product.model.Product;

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
		return products.stream().map(product -> toModel(product)).collect(Collectors.toList());
	}
	
}
