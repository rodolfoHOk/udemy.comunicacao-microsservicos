package br.com.cursoudemy.productapi.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ApiSecurity {

	public AuthUser getAuthUser() {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();
		return new ObjectMapper().convertValue(jwt.getClaim("authUser"), AuthUser.class);
	}
	
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
}
