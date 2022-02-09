package br.com.cursoudemy.productapi.core.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

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
		private String product;
	}
	
	@Getter
	@Setter
	public class RoutingKey {
		private String productStock;
		private String salesConfirmation;
	}
	
	@Getter
	@Setter
	public class Queue {
		private String productStock;
		private String salesConfirmation;
	}
	
}
