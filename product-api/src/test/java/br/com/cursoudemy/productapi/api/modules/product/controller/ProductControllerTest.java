package br.com.cursoudemy.productapi.api.modules.product.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cursoudemy.productapi.api.dto.SuccessResponse;
import br.com.cursoudemy.productapi.api.exceptionhandler.ApiExceptionHandler;
import br.com.cursoudemy.productapi.api.modules.product.assembler.ProductRequestDisassembler;
import br.com.cursoudemy.productapi.api.modules.product.assembler.ProductResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.product.assembler.ProductSalesResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductCheckStockRequest;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductRequest;
import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import br.com.cursoudemy.productapi.domain.modules.category.model.Category;
import br.com.cursoudemy.productapi.domain.modules.product.listener.dto.ProductQuantityDTO;
import br.com.cursoudemy.productapi.domain.modules.product.model.Product;
import br.com.cursoudemy.productapi.domain.modules.product.model.dto.ProductSales;
import br.com.cursoudemy.productapi.domain.modules.product.service.ProductService;
import br.com.cursoudemy.productapi.domain.modules.supplier.model.Supplier;
import br.com.cursoudemy.productapi.utils.JsonConversionUnit;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
	
	@Mock
	private ProductService productService;
	
	@InjectMocks
	private ProductController productController;
	
	private MockMvc mockMvc;
	
	private static final String BASE_URL = "/api/products";
	private static final Integer VALID_ID = 1;
	private static final Integer INVALID_ID = 100;
	private static final String VALID_NAME = "The Secret";
	private static final String INVALID_NAME = "The Legend of Zelda";
	
	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(productController)
				.setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
				.setControllerAdvice(new ApiExceptionHandler())
				.build();
	}
	
	@Test
	void shouldReturnCreatedAndCreatedProductWhenPostIsCalled() throws Exception {
		// given
		ProductRequest productRequest = createProductRequest();
		Product savedProduct = createProduct();
		String expectedJson = new ObjectMapper().writeValueAsString(
				ProductResponseAssembler.toModel(savedProduct));
		// when 
		when(productService.save(ProductRequestDisassembler.toDomainObject(productRequest)))
			.thenReturn(savedProduct);
		// then
		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(productRequest)))
			.andExpect(status().isCreated())
			.andExpect(content().string(expectedJson));
	}
	
	@Test
	void shouldReturnBadRequestWhenPostIsCalledWithoutRequiredField() throws Exception {
		ProductRequest productRequest = createProductRequest();
		productRequest.setName(null);
		
		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(productRequest)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnOkAndListOfProductWhenGetIsCalled() throws Exception {
		Product foundProduct = createProduct();
		
		when(productService.findAll()).thenReturn(Collections.singletonList(foundProduct));
		
		mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(equalTo(foundProduct.getId()))))
			.andExpect(jsonPath("$[0].name", is(equalTo(foundProduct.getName()))));
	}
	
	@Test
	void shouldReturnOkAndEmptyListWhenGetIsCalled() throws Exception {
		when(productService.findAll()).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	void shouldReturnOkAndAProductWhenGetByIdIsCalled() throws Exception {
		Product foundProduct = createProduct();
		
		when(productService.findById(VALID_ID)).thenReturn(foundProduct);
		
		mockMvc.perform(get(BASE_URL + "/" + VALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(foundProduct.getId()))))
			.andExpect(jsonPath("$.name", is(equalTo(foundProduct.getName()))));
	}
	
	@Test
	void shouldReturnNotFoundWhenGetByIdIsCalledWithInvalidId() throws Exception {
		when(productService.findById(INVALID_ID)).thenThrow(ResourceNotFoundException.class);
		
		mockMvc.perform(get(BASE_URL + "/" + INVALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnOkAndListOfProductWhenGetByNameIsCalled() throws Exception {
		Product foundProduct = createProduct();
		
		when(productService.findByName(VALID_NAME))
			.thenReturn(Collections.singletonList(foundProduct));
		
		mockMvc.perform(get(BASE_URL + "/name/" + VALID_NAME).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(equalTo(foundProduct.getId()))))
			.andExpect(jsonPath("$[0].name", is(equalTo(foundProduct.getName()))));
	}
	
	@Test
	void shouldReturnOkAndAEmptyListWhenGetByNameIsCalledWithInvalidName() throws Exception {
		when(productService.findByName(INVALID_NAME)).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(BASE_URL + "/name/" + INVALID_NAME).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	void shouldReturnOkAndASuccessResponseWhenDeleteByIdIsCalled() throws Exception {
		SuccessResponse expectedSuccessResponse = SuccessResponse.create("The product was deleted.");
		String expectedJson = new ObjectMapper().writeValueAsString(expectedSuccessResponse);
		
		doNothing().when(productService).delete(VALID_ID);
		
		mockMvc.perform(delete(BASE_URL + "/" + VALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(expectedJson));
	}
	
	@Test
	void shouldReturnNotFoundWhenDeleteByIdIsCalledWithInvalidId() throws Exception {
		doThrow(ResourceNotFoundException.class).when(productService).delete(INVALID_ID);
		
		mockMvc.perform(delete(BASE_URL + "/" + INVALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnOkAndUpdatedProductWhenUpdateByIdIsCalled() throws Exception {
		ProductRequest productRequest = createProductRequest();
		Product updatedProduct = createProduct();

		when(productService.update(ProductRequestDisassembler.toDomainObject(productRequest), VALID_ID))
			.thenReturn(updatedProduct);
		
		mockMvc.perform(put(BASE_URL + "/" + VALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(productRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(VALID_ID))))
			.andExpect(jsonPath("$.name", is(equalTo(productRequest.getName()))));
	}
	
	@Test
	void shouldReturnNotFoundWhenUpdateByIdIsCalledWithInvalidId() throws Exception {
		ProductRequest productRequest = createProductRequest();

		doThrow(ResourceNotFoundException.class).when(productService)
			.update(ProductRequestDisassembler.toDomainObject(productRequest), INVALID_ID);
		
		mockMvc.perform(put(BASE_URL + "/" + INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(productRequest)))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnBadRequestWhenUpdateByIdIsCalledWithoutRequiredField() throws Exception {
		ProductRequest productRequest = createProductRequest();
		productRequest.setName(null);
		
		mockMvc.perform(put(BASE_URL + "/" + INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(productRequest)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnOkAndProductWithSalesWhenGetByIdWithSales() throws Exception {
		ProductSales foundProductSales = createProductSales();
		String expectedJson = new ObjectMapper().writeValueAsString(
				ProductSalesResponseAssembler.toModel(foundProductSales));
		
		when(productService.findProductSales(VALID_ID)).thenReturn(foundProductSales);
				
		mockMvc.perform(get(BASE_URL + "/" + VALID_ID + "/sales").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(expectedJson));
	}
	
	@Test
	void shouldReturnNotFoundWhenWhenGetByIdWithSalesWithInvalidIdOrProductHasNoSales() throws Exception {
		when(productService.findProductSales(INVALID_ID)).thenThrow(ResourceNotFoundException.class);
		
		mockMvc.perform(get(BASE_URL + "/" + INVALID_ID + "/sales").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnNotFoundWhenWhenGetByIdWithSalesWithAValidIdAndProductHasNoSales() throws Exception {
		when(productService.findProductSales(VALID_ID)).thenThrow(ResourceNotFoundException.class);
		
		mockMvc.perform(get(BASE_URL + "/" + VALID_ID + "/sales").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnOkAndSuccessResponseWhenCheckProductsStock() throws Exception {
		Logger productControllerLogger = (Logger) LoggerFactory.getLogger(ProductController.class);
		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		productControllerLogger.addAppender(listAppender);
		List<ILoggingEvent> logsList = listAppender.list;
		
		ProductCheckStockRequest productCheckStockRequest = createProductCheckStockRequest();
		
		doNothing().when(productService).checkProductsStock(productCheckStockRequest.getProducts());
		
		mockMvc.perform(post(BASE_URL + "/check-stock")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(productCheckStockRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status", is(equalTo(200))))
			.andExpect(jsonPath("$.message", is(equalTo("The stock is ok"))));
		
		assertThat(logsList.get(0).getLevel(), is(equalTo(Level.INFO)));
		assertThat(logsList.get(0).getMessage(), 
				containsString("Request to POST product stock with data"));
		assertThat(logsList.get(1).getLevel(), is(equalTo(Level.INFO)));
		assertThat(logsList.get(1).getMessage(), 
				containsString("Response to POST product stock with data"));
	}
	
	@Test
	void shouldReturnBadRequestWhenCheckProductsStockAndThereAreNotEnoughProductsInStock() throws Exception {
		Logger productControllerLogger = (Logger) LoggerFactory.getLogger(ProductController.class);
		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		productControllerLogger.addAppender(listAppender);
		List<ILoggingEvent> logsList = listAppender.list;
		
		ProductCheckStockRequest productCheckStockRequest = createProductCheckStockRequest();
		
		doThrow(ValidationException.class).when(productService)
			.checkProductsStock(productCheckStockRequest.getProducts());
		
		mockMvc.perform(post(BASE_URL + "/check-stock")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(productCheckStockRequest)))
			.andExpect(status().isBadRequest());
		
		assertThat(logsList.get(0).getLevel(), is(equalTo(Level.INFO)));
		assertThat(logsList.get(0).getMessage(), 
				containsString("Request to POST product stock with data"));
		assertThat(logsList.size(), is(equalTo(1)));
	}
	
	@Test
	void shouldReturnNotFoundWhenCheckProductsStockAndAnInvalidProductIdIsEncountered() throws Exception {
		Logger productControllerLogger = (Logger) LoggerFactory.getLogger(ProductController.class);
		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		productControllerLogger.addAppender(listAppender);
		List<ILoggingEvent> logsList = listAppender.list;
		
		ProductCheckStockRequest productCheckStockRequest = createProductCheckStockRequest();
		
		doThrow(ResourceNotFoundException.class).when(productService)
			.checkProductsStock(productCheckStockRequest.getProducts());
		
		mockMvc.perform(post(BASE_URL + "/check-stock")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(productCheckStockRequest)))
			.andExpect(status().isNotFound());
		
		assertThat(logsList.get(0).getLevel(), is(equalTo(Level.INFO)));
		assertThat(logsList.get(0).getMessage(), 
				containsString("Request to POST product stock with data"));
		assertThat(logsList.size(), is(equalTo(1)));
	}
	
	private ProductCheckStockRequest createProductCheckStockRequest() {
		ProductQuantityDTO productQuantityDTO1 = new ProductQuantityDTO(1, 5);
		ProductQuantityDTO productQuantityDTO2 = new ProductQuantityDTO(2, 5);
		ProductQuantityDTO productQuantityDTO3 = new ProductQuantityDTO(3, 5);
		ProductCheckStockRequest productCheckStockRequest = new ProductCheckStockRequest();
		productCheckStockRequest.setProducts(List.of(productQuantityDTO1, productQuantityDTO2, productQuantityDTO3));
		
		return productCheckStockRequest;
	}
	
	private ProductSales createProductSales() {
		Product product = createProduct();
		String sale1Id = "f1c2956f-92b4-4d33-a2a5-1619e6547120";
		String sale2Id = "84a4793f-7163-495e-8620-11dbefbcd40d";
		String sale3Id = "1f0e574c-1fc8-40c3-bdb0-11d29ebb8002";
		
		return ProductSales.of(product, List.of(sale1Id, sale2Id, sale3Id));
	}
	
	private Product createProduct() {
		Product product = new Product();
		product.setId(VALID_ID);
		product.setName(VALID_NAME);
		product.setQuantityAvailable(10);
		Category category = new Category(1, "Hardware");
		product.setCategory(category);
		Supplier supplier = new Supplier(2, "Kabum WebStore");
		product.setSupplier(supplier);
		
		return product;
	}
	
	private ProductRequest createProductRequest() {
		ProductRequest productRequest = new ProductRequest();
		productRequest.setName(VALID_NAME);
		productRequest.setQuantityAvailable(10);
		productRequest.setCategoryId(1);
		productRequest.setSupplierId(2);
		
		return productRequest;
	}
	
}
