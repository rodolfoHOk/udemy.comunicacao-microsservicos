package br.com.cursoudemy.productapi.domain.client;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.cursoudemy.productapi.domain.client.dto.SalesProductResponse;

@FeignClient(name = "salesClient", contextId = "salesClient", url = "${app-config.services.sales}")
public interface SalesClient {

	@GetMapping("/api/orders/products/{productId}")
	Optional<SalesProductResponse> findSalesByProductId(@PathVariable Integer productId);
	
}
