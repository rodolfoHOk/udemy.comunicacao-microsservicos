package br.com.cursoudemy.productapi.api.modules.product.assembler;

import br.com.cursoudemy.productapi.api.modules.category.assembler.CategoryResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductSalesResponse;
import br.com.cursoudemy.productapi.api.modules.supplier.assembler.SupplierResponseAssembler;
import br.com.cursoudemy.productapi.domain.modules.product.model.dto.ProductSales;

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
