package br.com.cursoudemy.productapi.domain.modules.product.listener.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDTO {
	
	@ApiModelProperty(example = "1")
	@NotNull
	private Integer productId;
	
	@ApiModelProperty(example = "5")
	@NotNull
	@Positive
	private Integer quantity;
	
}
