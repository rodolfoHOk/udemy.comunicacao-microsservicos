package br.com.cursoudemy.productapi.domain.modules.sales.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cursoudemy.productapi.core.rabbitmq.RabbitProperties;
import br.com.cursoudemy.productapi.domain.modules.sales.sender.dto.SalesConfirmationDTO;
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
			log.info("Sending message with data: {} and TransactionId: {}",
					new ObjectMapper().writeValueAsString(salesConfirmationDTO),
					salesConfirmationDTO.getTransactionid());
			var productStockExchange = rabbitProperties.getExchange().getProduct();
			var salesConfirmationKey = rabbitProperties.getRoutingKey().getSalesConfirmation();
			rabbitTemplate.convertAndSend(productStockExchange, salesConfirmationKey, salesConfirmationDTO);
			log.info("Message was sent successfully [TransactionId: {}]", salesConfirmationDTO.getTransactionid());
		} catch (Exception ex) {
			log.error("Error while trying to send sales confirmation message: {} [TransactionId: {}]", ex,
						salesConfirmationDTO.getTransactionid());
		}
	}
}
