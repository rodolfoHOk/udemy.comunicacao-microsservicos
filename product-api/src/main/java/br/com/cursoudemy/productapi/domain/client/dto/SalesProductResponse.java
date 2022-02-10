package br.com.cursoudemy.productapi.domain.client.dto;

import java.util.List;

import lombok.Data;

@Data
public class SalesProductResponse {
	
	private List<String> salesIds;
	
}
