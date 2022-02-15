package br.com.cursoudemy.productapi.api.status;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusResponse {
	
	@ApiModelProperty(example = "Product-API")
	private String service;
	
	@ApiModelProperty(example = "up")
	private String status;
	
	@ApiModelProperty(example = "200")
	private Integer httpStatus;
	
}
