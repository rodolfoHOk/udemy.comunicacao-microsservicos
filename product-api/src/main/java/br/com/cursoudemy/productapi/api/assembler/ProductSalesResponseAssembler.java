package br.com.cursoudemy.productapi.api.assembler;

import br.com.cursoudemy.productapi.api.dto.ProductSalesResponse;
import br.com.cursoudemy.productapi.domain.model.dto.ProductSales;

public class ProductSalesResponseAssembler {
	
	public static ProductSalesResponse toModel (ProductSales productSales) {
		return ProductSalesResponse
				.builder()
				.id(productSales.getId())
				.name(productSales.getName())
				.quantityAvailable(productSales.getQuantityAvailable())
				.createdAt(productSales.getCreatedAt())
				.category(CategoryResponseAssembler.toModel(productSales.getCategory()))
				.supplier(SupplierResponseAssembler.toModel(productSales.getSupplier()))
				.sales(productSales.getSales())
				.build();
	}
}
