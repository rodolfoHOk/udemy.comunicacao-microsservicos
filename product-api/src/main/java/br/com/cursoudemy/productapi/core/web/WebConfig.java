package br.com.cursoudemy.productapi.core.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.cursoudemy.productapi.core.interceptor.TracingInterpector;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Bean
	public TracingInterpector tracingInterceptor() {
		return new TracingInterpector();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tracingInterceptor())
			.excludePathPatterns("/swagger-ui/**", "/v3/api-docs", "/swagger-resources/**", "/api/status");
	}

}
