package br.com.cursoudemy.productapi.domain.modules.product.listener;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
	}
	
	@Test
	void shouldReceiveProductStockMessageAndUpdateProductStockAndSendApprovedMessageToSalesConfirmationQueueWhenSendMessageInProductStockQueue()
			throws JsonProcessingException, InterruptedException {
		String productStockExchange = rabbitProperties.getExchange().getProduct();
		String productStockRoutingKey = rabbitProperties.getRoutingKey().getProductStock();
		ProductStockDTO productStockDTO = createProductStockDTO();
		SalesConfirmationDTO approvedSalesConfirmationDTO = createApprovedSalesConfirmationDTO();
		
		rabbitTemplate.convertAndSend(productStockExchange, productStockRoutingKey, productStockDTO);
		
		verify(productService, timeout(2000)).updateProductStock(productStockDTO);
		verify(salesConfirmationSender, timeout(2000)).sendSalesConfirmationMessage(approvedSalesConfirmationDTO);
	}
	
	@Test
	void shouldReceiveProductStockMessageAndSendRejectedMessageToSalesConfirmationQueueWhenSendMessageInProductStockQueueWithProductOutOfStock() 
			throws InterruptedException {
		String productStockExchange = rabbitProperties.getExchange().getProduct();
		String productStockRountingKey = rabbitProperties.getRoutingKey().getProductStock();
		ProductStockDTO productStockDTOOutOfStock = createProductStockDTOOutOfStock();
		SalesConfirmationDTO rejectedSalesConfirmationDTO = createRejectedSalesConfirmationDTO();
		
		rabbitTemplate.convertAndSend(productStockExchange, productStockRountingKey, productStockDTOOutOfStock);
		
		verify(productService, timeout(2000)).updateProductStock(productStockDTOOutOfStock);
		verify(salesConfirmationSender, timeout(2000)).sendSalesConfirmationMessage(rejectedSalesConfirmationDTO);
	}
	
	@Test
	void shouldReceiveProductStockMessageAndSendRejectedMessageToSalesConfirmationQueueWhenSendMessageInProductStockQueueWithInvalidProductId() 
			throws InterruptedException {
		String productStockExchange = rabbitProperties.getExchange().getProduct();
		String productStockRountingKey = rabbitProperties.getRoutingKey().getProductStock();
		ProductStockDTO productStockDTOWithInvalidProductId = createProductStockDTOWithInvalidProductId();
		SalesConfirmationDTO rejectedSalesConfirmationDTO = createRejectedSalesConfirmationDTO();
		
		rabbitTemplate.convertAndSend(productStockExchange, productStockRountingKey, productStockDTOWithInvalidProductId);
		
		verify(productService, timeout(2000)).updateProductStock(productStockDTOWithInvalidProductId);
		verify(salesConfirmationSender, timeout(2000)).sendSalesConfirmationMessage(rejectedSalesConfirmationDTO);
	}
	
	private SalesConfirmationDTO createApprovedSalesConfirmationDTO() {
		SalesConfirmationDTO salesConfirmationDTO = new SalesConfirmationDTO();
		salesConfirmationDTO.setSalesId("b1b42f73-0982-4e1b-ac2d-0d2c63ae3f48");
		salesConfirmationDTO.setTransactionid("e55671b7-0abf-4605-84ad-b6ea6cc71927");
		salesConfirmationDTO.setStatus(SalesStatus.APPROVED);
		
		return salesConfirmationDTO;
	}
	
	private SalesConfirmationDTO createRejectedSalesConfirmationDTO() {
		SalesConfirmationDTO salesConfirmationDTO = new SalesConfirmationDTO();
		salesConfirmationDTO.setSalesId("b1b42f73-0982-4e1b-ac2d-0d2c63ae3f48");
		salesConfirmationDTO.setTransactionid("e55671b7-0abf-4605-84ad-b6ea6cc71927");
		salesConfirmationDTO.setStatus(SalesStatus.REJECTED);
		
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
	
	private ProductStockDTO createProductStockDTOOutOfStock() {
		ProductStockDTO productStockDTO = new ProductStockDTO();
		ProductQuantityDTO productQuantityDTO1 = new ProductQuantityDTO(1001, 2);
		ProductQuantityDTO productQuantityDTO2 = new ProductQuantityDTO(1002, 1);
		ProductQuantityDTO productQuantityDTO3 = new ProductQuantityDTO(1003, 11);
		productStockDTO.setProducts(List.of(productQuantityDTO1, productQuantityDTO2, productQuantityDTO3));
		productStockDTO.setSalesId("b1b42f73-0982-4e1b-ac2d-0d2c63ae3f48");
		productStockDTO.setTransactionid("e55671b7-0abf-4605-84ad-b6ea6cc71927");
		
		return productStockDTO;
	}
	
	private ProductStockDTO createProductStockDTOWithInvalidProductId() {
		ProductStockDTO productStockDTO = new ProductStockDTO();
		ProductQuantityDTO productQuantityDTO1 = new ProductQuantityDTO(1001, 2);
		ProductQuantityDTO productQuantityDTO2 = new ProductQuantityDTO(1002, 1);
		ProductQuantityDTO productQuantityDTO3 = new ProductQuantityDTO(1100, 1);
		productStockDTO.setProducts(List.of(productQuantityDTO1, productQuantityDTO2, productQuantityDTO3));
		productStockDTO.setSalesId("b1b42f73-0982-4e1b-ac2d-0d2c63ae3f48");
		productStockDTO.setTransactionid("e55671b7-0abf-4605-84ad-b6ea6cc71927");
		
		return productStockDTO;
	}
}
