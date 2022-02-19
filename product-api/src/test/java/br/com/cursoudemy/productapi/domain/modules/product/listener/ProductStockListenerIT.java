package br.com.cursoudemy.productapi.domain.modules.product.listener;

import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.cursoudemy.productapi.core.rabbitmq.RabbitProperties;
import br.com.cursoudemy.productapi.domain.modules.product.listener.dto.ProductQuantityDTO;
import br.com.cursoudemy.productapi.domain.modules.product.listener.dto.ProductStockDTO;
import br.com.cursoudemy.productapi.domain.modules.product.service.ProductService;
import br.com.cursoudemy.productapi.domain.modules.sales.sender.SalesConfirmationSender;
import br.com.cursoudemy.productapi.domain.modules.sales.sender.dto.SalesConfirmationDTO;
import br.com.cursoudemy.productapi.domain.modules.sales.sender.enums.SalesStatus;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class ProductStockListenerIT {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	@Autowired
	private RabbitProperties rabbitProperties;
	
	@Autowired
	private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
	
	@SpyBean
	private ProductService productService;
	
	@SpyBean
	private SalesConfirmationSender salesConfirmationSender;
	
	@BeforeEach
	void setUp() {
		rabbitAdmin.purgeQueue(rabbitProperties.getQueue().getProductStock(), true);
	}
	
	@AfterEach
	void tearDown() {
		rabbitAdmin.purgeQueue(rabbitProperties.getQueue().getProductStock(), true);
		rabbitListenerEndpointRegistry.stop();
	}
	
	@Test
	void shouldReceiveProductStockMessageAndUpdateProductStockAndSendApprovedMessageToSalesConfirmationQueueWhenSendMessageProductStockDTOInQueue()
			throws JsonProcessingException, InterruptedException {
		String productStockExchange = rabbitProperties.getExchange().getProduct();
		String productStockRoutingKey = rabbitProperties.getRoutingKey().getProductStock();
		ProductStockDTO productStockDTO = createProductStockDTO();
		SalesConfirmationDTO salesConfirmationDTO = createApprovedSalesConfirmationDTO();
		
		rabbitTemplate.convertAndSend(productStockExchange, productStockRoutingKey, productStockDTO);
		TimeUnit.SECONDS.sleep(1);
		rabbitListenerEndpointRegistry.getListenerContainer("update-product-stock-queue-listener").start();
		TimeUnit.SECONDS.sleep(2);
		
		verify(productService).updateProductStock(productStockDTO);
		verify(salesConfirmationSender).sendSalesConfirmationMessage(salesConfirmationDTO);
	}
	
	private SalesConfirmationDTO createApprovedSalesConfirmationDTO() {
		SalesConfirmationDTO salesConfirmationDTO = new SalesConfirmationDTO();
		salesConfirmationDTO.setSalesId("b1b42f73-0982-4e1b-ac2d-0d2c63ae3f48");
		salesConfirmationDTO.setTransactionid("e55671b7-0abf-4605-84ad-b6ea6cc71927");
		salesConfirmationDTO.setStatus(SalesStatus.APPROVED);
		
		return salesConfirmationDTO;
	}
	
	private ProductStockDTO createProductStockDTO() {
		ProductStockDTO productStockDTO = new ProductStockDTO();
		ProductQuantityDTO productQuantityDTO1 = new ProductQuantityDTO(1001, 2);
		ProductQuantityDTO productQuantityDTO2 = new ProductQuantityDTO(1002, 1);
		ProductQuantityDTO productQuantityDTO3 = new ProductQuantityDTO(1003, 1);
		productStockDTO.setProducts(List.of(productQuantityDTO1, productQuantityDTO2, productQuantityDTO3));
		productStockDTO.setSalesId("b1b42f73-0982-4e1b-ac2d-0d2c63ae3f48");
		productStockDTO.setTransactionid("e55671b7-0abf-4605-84ad-b6ea6cc71927");
		
		return productStockDTO;
	}
}
