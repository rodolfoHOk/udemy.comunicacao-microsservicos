package br.com.cursoudemy.productapi.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class ProductSalesResponse {
	
	private Integer id;
	
	private String name;
	
	private Integer quantityAvailable;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private OffsetDateTime createdAt;
	
	private CategoryResponse category;
	
	private SupplierResponse supplier;
	
	private List<String> sales;
}
