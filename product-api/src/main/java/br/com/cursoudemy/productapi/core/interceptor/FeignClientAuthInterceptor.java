package br.com.cursoudemy.productapi.core.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.cursoudemy.productapi.domain.exception.ClientRequestException;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {
	
	private static final String AUTHORIZATION = "Authorization";
	
	@Override
	public void apply(RequestTemplate template) {
		template.header(AUTHORIZATION, getCurrentRequest().getHeader(AUTHORIZATION));
	}
	
	private HttpServletRequest getCurrentRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ClientRequestException("The current request could not be proccessed");
		}
	}

}
