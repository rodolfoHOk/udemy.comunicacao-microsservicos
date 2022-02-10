package br.com.cursoudemy.productapi.domain.model.dto;

import java.time.OffsetDateTime;
import java.util.List;

import br.com.cursoudemy.productapi.domain.model.Category;
import br.com.cursoudemy.productapi.domain.model.Product;
import br.com.cursoudemy.productapi.domain.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSales {
	
	private Integer id;
	
	private String name;
	
	private Integer quantityAvailable;
	
	private OffsetDateTime createdAt;
	
	private Category category;
	
	private Supplier supplier;
	
	private List<String> sales;
	
	
	public static ProductSales of (Product product, List<String> sales) {
		return ProductSales
				.builder()
				.id(product.getId())
				.name(product.getName())
				.quantityAvailable(product.getQuantityAvailable())
				.createdAt(product.getCreatedAt())
				.category(product.getCategory())
				.supplier(product.getSupplier())
				.sales(sales)
				.build();
	}
	
}