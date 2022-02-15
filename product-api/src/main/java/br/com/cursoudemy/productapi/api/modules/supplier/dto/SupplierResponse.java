package br.com.cursoudemy.productapi.api.modules.supplier.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierResponse {
	
	@ApiModelProperty(example = "2")
	private Integer id;
	
	@ApiModelProperty(example = "Saraiva bookstore")
	private String name;
	
}
