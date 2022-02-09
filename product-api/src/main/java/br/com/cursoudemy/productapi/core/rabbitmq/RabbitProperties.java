package br.com.cursoudemy.productapi.core.rabbitmq;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("app-config.rabbit")
public class RabbitProperties {
	
	private Exchange exchange = new Exchange();
	private RoutingKey routingKey = new RoutingKey();
	private Queue queue = new Queue();
	
	@Getter
	@Setter
	public class Exchange {
		@NotNull
		private String product;
	}
	
	@Getter
	@Setter
	public class RoutingKey {
		@NotNull
		private String productStock;
		@NotNull
		private String salesConfirmation;
	}
	
	@Getter
	@Setter
	public class Queue {
		@NotNull
		private String productStock;
		@NotNull
		private String salesConfirmation;
	}
	
}
