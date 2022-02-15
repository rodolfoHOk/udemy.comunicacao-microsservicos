package br.com.cursoudemy.productapi.api.modules.product.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cursoudemy.productapi.api.modules.category.dto.CategoryResponse;
import br.com.cursoudemy.productapi.api.modules.supplier.dto.SupplierResponse;
import io.swagger.annotations.ApiModelProperty;
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
	
	@ApiModelProperty(example = "1")
	private Integer id;
	
	@ApiModelProperty(example = "The Secret")
	private String name;
	
	@ApiModelProperty(example = "10")
	private Integer quantityAvailable;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private OffsetDateTime createdAt;
	
	private CategoryResponse category;
	
	private SupplierResponse supplier;
	
	@ApiModelProperty(example = "['dsdsd45v6w1rv15c61','456d15rv1r51c54w1c5']")
	private List<String> sales;
}
