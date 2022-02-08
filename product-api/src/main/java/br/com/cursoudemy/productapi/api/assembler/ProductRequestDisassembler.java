package br.com.cursoudemy.productapi.api.assembler;

import br.com.cursoudemy.productapi.api.dto.ProductRequest;
import br.com.cursoudemy.productapi.domain.model.Category;
import br.com.cursoudemy.productapi.domain.model.Product;
import br.com.cursoudemy.productapi.domain.model.Supplier;

public class ProductRequestDisassembler {
	
	public static Product toDomainObject (ProductRequest productRequest) {
		var product = new Product();
		product.setName(productRequest.getName());
		product.setQuantityAvailable(productRequest.getQuantityAvailable());
		var category = new Category();
		category.setId(productRequest.getCategoryId());
		product.setCategory(category);
		var supplier = new Supplier();
		supplier.setId(productRequest.getSupplierId());
		product.setSupplier(supplier);
		
		return product;
	}
}
