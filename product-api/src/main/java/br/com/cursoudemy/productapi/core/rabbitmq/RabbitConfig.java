package br.com.cursoudemy.productapi.core.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.validation.SmartValidator;

@Configuration
public class RabbitConfig {
	
	@Autowired
	private RabbitProperties rabbitProperties;
	
	@Autowired
	private SmartValidator validator;
	
	@Bean
	public TopicExchange productTopicExchange() {
		return new TopicExchange(rabbitProperties.getExchange().getProduct());
	}
	
	@Bean
	public Queue productStockMq() {
		return new Queue(rabbitProperties.getQueue().getProductStock(), true);
	}
	
	@Bean
	public Queue salesConfirmationMq() {
		return new Queue(rabbitProperties.getQueue().getSalesConfirmation(), true);
	}
	
	@Bean
	public Binding productStockMqBinding(TopicExchange topicExchange) {
		return BindingBuilder
				.bind(productStockMq())
				.to(topicExchange)
				.with(rabbitProperties.getRoutingKey().getProductStock());
	}

	@Bean
	public Binding salesConfirmationMqBinding(TopicExchange topicExchange) {
		return BindingBuilder
				.bind(salesConfirmationMq())
				.to(topicExchange)
				.with(rabbitProperties.getRoutingKey().getSalesConfirmation());
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	// For Validate payload (dto) in Listener
	@Bean
	public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
	    DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
	    factory.setValidator(validator);
	    return factory;
	}
	
}
