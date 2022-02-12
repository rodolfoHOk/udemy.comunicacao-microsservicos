package br.com.cursoudemy.productapi.core.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.cursoudemy.productapi.domain.exception.ValidationException;

public class TracingInterpector implements HandlerInterceptor {
	
	private static final String TRANSACTION_ID = "transactionid";
	private static final String SERVICE_ID = "serviceid";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (isOptions(request)) {
			return true;
		}
		if (ObjectUtils.isEmpty(request.getHeader(TRANSACTION_ID))) {
			throw new ValidationException("The transactionid header is required");
		}
		request.setAttribute(SERVICE_ID, UUID.randomUUID().toString());
		return true;
	}
	
	private boolean isOptions(HttpServletRequest request) {
		return HttpMethod.OPTIONS.name().equals(request.getMethod());
	}

}
