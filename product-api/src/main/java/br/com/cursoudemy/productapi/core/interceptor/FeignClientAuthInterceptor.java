package br.com.cursoudemy.productapi.core.interceptor;

import org.springframework.stereotype.Component;

import br.com.cursoudemy.productapi.core.utils.RequestUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {
	
	private static final String AUTHORIZATION = "Authorization";
	private static final String TRANSACTION_ID = "transactionid";
	
	@Override
	public void apply(RequestTemplate template) {
		template
			.header(AUTHORIZATION, RequestUtils.getCurrentRequest().getHeader(AUTHORIZATION))
			.header(TRANSACTION_ID, RequestUtils.getCurrentRequest().getHeader(TRANSACTION_ID));
	}

}
