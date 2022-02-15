package br.com.cursoudemy.productapi.api.status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController implements StatusControllerOpenApi {

	@GetMapping
	public ResponseEntity<StatusResponse> getApiStatus() {
		var statusResponse = new StatusResponse();
		
		statusResponse.setService("Product-API");
		statusResponse.setStatus("up");
		statusResponse.setHttpStatus(HttpStatus.OK.value());
		
		return ResponseEntity.ok(statusResponse);
	}
}
