package br.com.cursoudemy.productapi.api.modules.product.dto;

import java.time.OffsetDateTime;

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
public class ProductResponse {

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
	
}
