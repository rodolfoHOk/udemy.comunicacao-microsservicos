package br.com.cursoudemy.productapi.domain.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cursoudemy.productapi.core.rabbitmq.RabbitProperties;
import br.com.cursoudemy.productapi.domain.sender.dto.SalesConfirmationDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SalesConfirmationSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private RabbitProperties rabbitProperties;
	
	public void sendSalesConfirmationMessage(SalesConfirmationDTO salesConfirmationDTO) {
		try {
			log.info("Sending message: {}", new ObjectMapper().writeValueAsString(salesConfirmationDTO));
			var productStockExchange = rabbitProperties.getExchange().getProduct();
			var salesConfirmationKey = rabbitProperties.getRoutingKey().getSalesConfirmation();
			rabbitTemplate.convertAndSend(productStockExchange, salesConfirmationKey, salesConfirmationDTO);
			log.info("Message was sent successfully");
		} catch (Exception ex) {
			log.error("Error while trying to send sales confirmation message: ", ex);
		}
	}
}
