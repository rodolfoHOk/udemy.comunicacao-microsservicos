package br.com.cursoudemy.productapi.core.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.cursoudemy.productapi.domain.exception.ClientRequestException;

public class RequestUtils {

	public static HttpServletRequest getCurrentRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ClientRequestException("The current request could not be proccessed");
		}
	}
	
}
