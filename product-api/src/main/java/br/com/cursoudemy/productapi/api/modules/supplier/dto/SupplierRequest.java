package br.com.cursoudemy.productapi.api.modules.supplier.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequest {
	
	@NotBlank
	private String name;
	
}
