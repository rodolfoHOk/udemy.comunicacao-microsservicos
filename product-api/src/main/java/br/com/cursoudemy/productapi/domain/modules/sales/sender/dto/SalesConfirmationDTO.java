package br.com.cursoudemy.productapi.domain.modules.sales.sender.dto;

import br.com.cursoudemy.productapi.domain.modules.sales.sender.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesConfirmationDTO {

	private String salesId;
	
	private SalesStatus status;
	
	private String transactionid;
	
}
