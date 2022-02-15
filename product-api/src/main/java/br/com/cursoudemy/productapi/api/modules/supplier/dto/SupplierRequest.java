package br.com.cursoudemy.productapi.api.modules.supplier.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequest {
	
	@ApiModelProperty(example = "Saraiva bookstore")
	@NotBlank
	private String name;
	
}
