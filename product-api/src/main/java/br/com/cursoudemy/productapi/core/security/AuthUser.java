package br.com.cursoudemy.productapi.core.security;

import lombok.Data;

@Data
public class AuthUser {

	private Integer id;
	
	private String name;
	
	private String email;
	
}
