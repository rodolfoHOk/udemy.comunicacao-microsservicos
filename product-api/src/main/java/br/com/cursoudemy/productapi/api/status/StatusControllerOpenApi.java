package br.com.cursoudemy.productapi.api.status;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Status")
public interface StatusControllerOpenApi {
	
	@ApiOperation("Request API Status")
	ResponseEntity<StatusResponse> getApiStatus();
	
}