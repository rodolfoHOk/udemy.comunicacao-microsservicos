package br.com.cursoudemy.productapi.domain.modules.sales.sender;

import static org.hamcrest.Matchers.is;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.cursoudemy.productapi.core.rabbitmq.RabbitProperties;
import br.com.cursoudemy.productapi.domain.modules.sales.sender.dto.SalesConfirmationDTO;
import br.com.cursoudemy.productapi.domain.modules.sales.sender.enums.SalesStatus;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class SalesConfirmationSenderIT {
	
	@Autowired
	private RabbitProperties rabbitProperties;
	
	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private SalesConfirmationSender salesConfirmationSender;
	
	private static final String TRANSACTION_ID = "e55671b7-0abf-4605-84ad-b6ea6cc71927";
	private static final String SALES_ID = "b1b42f73-0982-4e1b-ac2d-0d2c63ae3f48";
	
	@BeforeEach
	void setUp() {
		rabbitAdmin.purgeQueue(rabbitProperties.getQueue().getSalesConfirmation(), true);
	}
	
	@AfterEach
	void tearDown() {
		rabbitAdmin.purgeQueue(rabbitProperties.getQueue().getSalesConfirmation(), true);
	}
	
	@Test
	void shouldReceiveSalesConfirmationMessageCallbackWhenSendSalesConfirmationMessage() throws InterruptedException {
		SalesConfirmationDTO salesConfirmationDTO = createSalesConfirmationDTO();
		
		salesConfirmationSender.sendSalesConfirmationMessage(salesConfirmationDTO);
		
		Awaitility.await().atMost(2, TimeUnit.SECONDS).until(isMessagePublishedInQueue(), is(true));
	}
	
	private Callable<Boolean> isMessagePublishedInQueue() {
		return () -> {
			var messageCount = rabbitTemplate.execute(channel -> channel
					.queueDeclare(rabbitProperties.getQueue().getSalesConfirmation(), true, false, false, null))
					.getMessageCount();
			
			var messageQueued = (SalesConfirmationDTO) rabbitTemplate
					.receiveAndConvert(rabbitProperties.getQueue().getSalesConfirmation());
		
			return messageCount == 1 
					&& messageQueued.getTransactionid().equals(TRANSACTION_ID) 
					&& messageQueued.getSalesId().equals(SALES_ID);
		};
	}
	
	private SalesConfirmationDTO createSalesConfirmationDTO() {
		SalesConfirmationDTO salesConfirmationDTO = new SalesConfirmationDTO();
		salesConfirmationDTO.setSalesId(SALES_ID);
		salesConfirmationDTO.setTransactionid(TRANSACTION_ID);
		salesConfirmationDTO.setStatus(SalesStatus.APPROVED);
		
		return salesConfirmationDTO;
	}
}
