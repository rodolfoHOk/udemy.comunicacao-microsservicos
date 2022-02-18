package br.com.cursoudemy.productapi.domain.modules.product.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import br.com.cursoudemy.productapi.domain.modules.category.model.Category;
import br.com.cursoudemy.productapi.domain.modules.category.service.CategoryService;
import br.com.cursoudemy.productapi.domain.modules.product.listener.dto.ProductQuantityDTO;
import br.com.cursoudemy.productapi.domain.modules.product.listener.dto.ProductStockDTO;
import br.com.cursoudemy.productapi.domain.modules.product.model.Product;
import br.com.cursoudemy.productapi.domain.modules.product.model.dto.ProductSales;
import br.com.cursoudemy.productapi.domain.modules.product.repository.ProductRepository;
import br.com.cursoudemy.productapi.domain.modules.sales.client.SalesClient;
import br.com.cursoudemy.productapi.domain.modules.sales.client.dto.SalesProductResponse;
import br.com.cursoudemy.productapi.domain.modules.sales.sender.SalesConfirmationSender;
import br.com.cursoudemy.productapi.domain.modules.supplier.model.Supplier;
import br.com.cursoudemy.productapi.domain.modules.supplier.service.SupplierService;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductService productService;
	
	@Mock
	private CategoryService categoryService;
	
	@Mock
	private SupplierService supplierService;
	
	@Mock
	private SalesConfirmationSender salesConfirmationSender;
	
	@Mock
	private SalesClient salesClient;
	
	private static final Integer VALID_PRODUCT_ID = 3;
	private static final Integer INVALID_PRODUCT_ID = 100;
	private static final Integer VALID_PRODUCT_ID_1 = 4;
	private static final Integer VALID_PRODUCT_ID_2 = 5;
	private static final Integer VALID_PRODUCT_ID_3 = 6;
	private static final Integer TEN_PRODUCT_STOCK = 10;
	private static final Integer FIVE_PRODUCT_STOCK = 5;
	private static final Integer ONE_PRODUCT_STOCK = 1;
	private static final String SALES_ID = "b1b42f73-0982-4e1b-ac2d-0d2c63ae3f48";
	private static final String TRANSACTION_ID = "e55671b7-0abf-4605-84ad-b6ea6cc71927";
	private static final String VALID_PRODUCT_NAME = "The Secret";
	private static final String INVALID_PRODUCT_NAME = "The Legend of Zelda";
	
	
	@Test
	void shouldReturnCreatedProductWhenSaveAProduct() {
		Product productToSave = createProduct();
		Product expectedCreatedProduct = createProductWithId(VALID_PRODUCT_ID);
		
		when(productRepository.save(productToSave)).thenReturn(expectedCreatedProduct);
		
		Product createdProduct = productService.save(productToSave);
		
		assertThat(createdProduct.getId(), is(equalTo(expectedCreatedProduct.getId())));
		assertThat(createdProduct.getName(), is(equalTo(expectedCreatedProduct.getName())));
	}
	
	@Test
	void shouldReturnAListOfProductsWhenFindAllProducts() {
		Product expectedFoundProduct= createProductWithId(VALID_PRODUCT_ID);
		
		when(productRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundProduct));
		
		List<Product> findProducts = productService.findAll();
		
		assertThat(findProducts, is(not(empty())));
		assertThat(findProducts.get(0), is(equalTo(expectedFoundProduct)));
	}
	
	@Test
	void shouldReturnAEmptyListWhenFindAllProducts() {
		when(productRepository.findAll()).thenReturn(Collections.emptyList());
		
		List<Product> findProducts = productService.findAll();
		
		assertThat(findProducts, is(empty()));
	}
	
	@Test
	void shouldReturnAProductWhenFindProductByIdWithAValidId() {
		Product expectedFoundProduct = createProductWithId(VALID_PRODUCT_ID);
		
		when(productRepository.findById(VALID_PRODUCT_ID)).thenReturn(Optional.of(expectedFoundProduct));
		
		Product foundProduct = productService.findById(VALID_PRODUCT_ID);
		
		assertThat(foundProduct, is(equalTo(expectedFoundProduct)));
	}
	
	@Test
	void shouldThrowResourceNotFoundExceptionWhenFindProductByIdWithAInvalidId() {
		when(productRepository.findById(INVALID_PRODUCT_ID)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> productService.findById(INVALID_PRODUCT_ID));
	}
	
	@Test
	void shouldReturnAListOfProductsWhenFindProductByNameWithAValidName() {
		Product expectedFoundProduct = createProductWithId(VALID_PRODUCT_ID);
		
		when(productRepository.findByNameIgnoreCaseContaining(VALID_PRODUCT_NAME))
			.thenReturn(Collections.singletonList(expectedFoundProduct));
		
		List<Product> findProducts = productService.findByName(VALID_PRODUCT_NAME);
		
		assertThat(findProducts, is(not(empty())));
		assertThat(findProducts.get(0), is(equalTo(expectedFoundProduct)));
	}

	@Test
	void shouldReturnAEmptyListWhenFindProductByNameWithAInvalidName() {
		when(productRepository.findByNameIgnoreCaseContaining(INVALID_PRODUCT_NAME))
			.thenReturn(Collections.emptyList());
		
		List<Product> findProducts = productService.findByName(INVALID_PRODUCT_NAME);
		
		assertThat(findProducts, is(empty()));
	}
	
	@Test
	void shouldDeleteAProductWhenDeleteProductWithAValidId() {
		when(productRepository.existsById(VALID_PRODUCT_ID)).thenReturn(true);
		doNothing().when(productRepository).deleteById(VALID_PRODUCT_ID);
		
		assertDoesNotThrow(() -> productService.delete(VALID_PRODUCT_ID));
	}
	
	@Test
	void shouldThrowResourceNotFoundExceptionWhenDeleteProductWithAInvalidId() {
		when(productRepository.existsById(INVALID_PRODUCT_ID)).thenReturn(false);
		
		assertThrows(ResourceNotFoundException.class, () -> productService.delete(INVALID_PRODUCT_ID));
	}
	
	@Test
	void shouldReturnAUpdatedProductWhenUpdateProductWithAValidId() {
		Product expectedUpdatedProduct= createProductWithId(VALID_PRODUCT_ID);
		
		when(productRepository.existsById(VALID_PRODUCT_ID)).thenReturn(true);
		when(productRepository.save(expectedUpdatedProduct)).thenReturn(expectedUpdatedProduct);
		
		Product updatedProduct = productService.update(expectedUpdatedProduct, VALID_PRODUCT_ID);
		
		assertThat(updatedProduct, is(equalTo(expectedUpdatedProduct)));	
	}
	
	@Test
	void shouldThrowResourceNotFoundExceptionWhenUpdateProductWithAInvalidId() {
		Product expectedUpdatedProduct = createProductWithId(VALID_PRODUCT_ID);
		
		when(productRepository.existsById(INVALID_PRODUCT_ID)).thenReturn(false);
		
		assertThrows(ResourceNotFoundException.class, () -> productService.update(expectedUpdatedProduct ,INVALID_PRODUCT_ID));
	}
	
	@Test
	void shouldUpdateTheProductsInStockAndLogAInfoWhenThereAreEnoughProductsInStock() {
		Logger productServiceLogger = (Logger) LoggerFactory.getLogger(ProductService.class);
		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		productServiceLogger.addAppender(listAppender);
		List<ILoggingEvent> logsList = listAppender.list;
		
		ProductStockDTO productStockDTO = createProductStockDTO();
		Product product1 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_1, TEN_PRODUCT_STOCK);
		Product product2 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_2, TEN_PRODUCT_STOCK);
		Product product3 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_3, TEN_PRODUCT_STOCK);
		
		when(productRepository.findById(VALID_PRODUCT_ID_1)).thenReturn(Optional.of(product1));
		when(productRepository.findById(VALID_PRODUCT_ID_2)).thenReturn(Optional.of(product2));
		when(productRepository.findById(VALID_PRODUCT_ID_3)).thenReturn(Optional.of(product3));
		doNothing().when(salesConfirmationSender).sendSalesConfirmationMessage(any());
		
		productService.updateProductStock(productStockDTO);
		
		assertThat(product1.getQuantityAvailable(), is(equalTo(FIVE_PRODUCT_STOCK)));
		assertThat(product2.getQuantityAvailable(), is(equalTo(FIVE_PRODUCT_STOCK)));
		assertThat(product3.getQuantityAvailable(), is(equalTo(FIVE_PRODUCT_STOCK)));
		assertThat(Level.INFO, is(equalTo(logsList.get(0).getLevel())));
		assertThat(logsList.get(0).getMessage(), containsString("Stock updated successfully"));
	}
	
	@Test
	void shouldLogAErrorWhenThereAreNotEnoughProductsInStock() {
		Logger productServiceLogger = (Logger) LoggerFactory.getLogger(ProductService.class);
		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		productServiceLogger.addAppender(listAppender);
		List<ILoggingEvent> logsList = listAppender.list;
		
		ProductStockDTO productStockDTO = createProductStockDTO();
		Product product1 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_1, TEN_PRODUCT_STOCK);
		Product product2 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_2, TEN_PRODUCT_STOCK);
		Product product3 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_3, ONE_PRODUCT_STOCK);
		
		when(productRepository.findById(VALID_PRODUCT_ID_1)).thenReturn(Optional.of(product1));
		when(productRepository.findById(VALID_PRODUCT_ID_2)).thenReturn(Optional.of(product2));
		when(productRepository.findById(VALID_PRODUCT_ID_3)).thenReturn(Optional.of(product3));
		doNothing().when(salesConfirmationSender).sendSalesConfirmationMessage(any());
		
		productService.updateProductStock(productStockDTO);
		
		assertThat(Level.ERROR, is(equalTo(logsList.get(0).getLevel())));
		assertThat(logsList.get(0).getMessage(), containsString("Error while trying to update stock for message"));
	}
	
	@Test
	void shouldLogAErrorWhenAnInvalidProductIdIsEncountered() {
		Logger productServiceLogger = (Logger) LoggerFactory.getLogger(ProductService.class);
		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		productServiceLogger.addAppender(listAppender);
		List<ILoggingEvent> logsList = listAppender.list;
		
		ProductStockDTO productStockDTO = createProductStockDTOWithInvalidId();
		Product product1 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_1, TEN_PRODUCT_STOCK);
		Product product2 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_2, TEN_PRODUCT_STOCK);
		
		when(productRepository.findById(VALID_PRODUCT_ID_1)).thenReturn(Optional.of(product1));
		when(productRepository.findById(VALID_PRODUCT_ID_2)).thenReturn(Optional.of(product2));
		when(productRepository.findById(INVALID_PRODUCT_ID)).thenReturn(Optional.empty());
		doNothing().when(salesConfirmationSender).sendSalesConfirmationMessage(any());
		
		productService.updateProductStock(productStockDTO);
		
		assertThat(Level.ERROR, is(equalTo(logsList.get(0).getLevel())));
		assertThat(logsList.get(0).getMessage(), containsString("Error while trying to update stock for message"));
	}
	
	@Test
	void shouldReturnAProductWithSalesAndLogInfosWhenFindProductSalesWithAValidIDAndProductHasSales() {
		Logger productServiceLogger = (Logger) LoggerFactory.getLogger(ProductService.class);
		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		productServiceLogger.addAppender(listAppender);
		List<ILoggingEvent> logsList = listAppender.list;
		
		Product expectedFoundProduct = createProductWithId(VALID_PRODUCT_ID);
		SalesProductResponse expectedFoundSalesOfProduct = createSalesProductResponse();
		ProductSales expectedProductSales = ProductSales.of(expectedFoundProduct, expectedFoundSalesOfProduct.getSalesIds());
		
		when(productRepository.findById(VALID_PRODUCT_ID)).thenReturn(Optional.of(expectedFoundProduct));
		when(salesClient.findSalesByProductId(VALID_PRODUCT_ID)).thenReturn(Optional.of(expectedFoundSalesOfProduct));
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		ProductSales foundProductSales = productService.findProductSales(VALID_PRODUCT_ID);
		
		assertThat(foundProductSales.getId(), is(equalTo(expectedProductSales.getId())));
		assertThat(foundProductSales.getName(), is(equalTo(expectedProductSales.getName())));
		assertThat(foundProductSales.getSales(), is(equalTo(expectedProductSales.getSales())));
		assertThat(Level.INFO, is(equalTo(logsList.get(0).getLevel())));
		assertThat(logsList.get(0).getMessage(), containsString("Sending GET request to orders by product ID"));
		assertThat(Level.INFO, is(equalTo(logsList.get(1).getLevel())));
		assertThat(logsList.get(1).getMessage(), containsString("Receiving GET request to orders by product ID"));
	}
	
	@Test
	void shouldThrowAResourceNotFoundExceptionWhenFindProductSalesWithAInvalidID() {
		when(productRepository.findById(INVALID_PRODUCT_ID)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> productService.findProductSales(INVALID_PRODUCT_ID));
	}
	
	@Test
	void shouldThrowAResourceNotFoundExceptionWhenFindProductSalesWithAValidIdAndProductHasNoSales() {
		Product expectedFoundProduct = createProductWithId(VALID_PRODUCT_ID);
		
		when(productRepository.findById(VALID_PRODUCT_ID)).thenReturn(Optional.of(expectedFoundProduct));
		when(salesClient.findSalesByProductId(VALID_PRODUCT_ID)).thenReturn(Optional.empty());
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		assertThrows(ResourceNotFoundException.class, () -> productService.findProductSales(VALID_PRODUCT_ID));
	}
	
	@Test
	void shouldNothingThrowWhenCheckProductsStockAndThereAreEnoughProductsInStock() {
		ProductQuantityDTO productQuantityDTO1 = new ProductQuantityDTO(VALID_PRODUCT_ID_1, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO2 = new ProductQuantityDTO(VALID_PRODUCT_ID_2, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO3 = new ProductQuantityDTO(VALID_PRODUCT_ID_3, FIVE_PRODUCT_STOCK);
		List<ProductQuantityDTO> productsQuantity = List.of(productQuantityDTO1, productQuantityDTO2, productQuantityDTO3);
		Product product1 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_1, TEN_PRODUCT_STOCK);
		Product product2 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_2, TEN_PRODUCT_STOCK);
		Product product3 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_3, TEN_PRODUCT_STOCK);
		
		when(productRepository.findById(VALID_PRODUCT_ID_1)).thenReturn(Optional.of(product1));
		when(productRepository.findById(VALID_PRODUCT_ID_2)).thenReturn(Optional.of(product2));
		when(productRepository.findById(VALID_PRODUCT_ID_3)).thenReturn(Optional.of(product3));
		
		assertDoesNotThrow(() -> productService.checkProductsStock(productsQuantity));		
	}
	
	@Test
	void shouldThrowValidationExceptionWhenCheckProductsStockAndThereAreNotEnoughProductsInStock() {
		ProductQuantityDTO productQuantityDTO1 = new ProductQuantityDTO(VALID_PRODUCT_ID_1, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO2 = new ProductQuantityDTO(VALID_PRODUCT_ID_2, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO3 = new ProductQuantityDTO(VALID_PRODUCT_ID_3, FIVE_PRODUCT_STOCK);
		List<ProductQuantityDTO> productsQuantity = List.of(productQuantityDTO1, productQuantityDTO2, productQuantityDTO3);
		Product product1 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_1, TEN_PRODUCT_STOCK);
		Product product2 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_2, TEN_PRODUCT_STOCK);
		Product product3 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_3, ONE_PRODUCT_STOCK);
		
		when(productRepository.findById(VALID_PRODUCT_ID_1)).thenReturn(Optional.of(product1));
		when(productRepository.findById(VALID_PRODUCT_ID_2)).thenReturn(Optional.of(product2));
		when(productRepository.findById(VALID_PRODUCT_ID_3)).thenReturn(Optional.of(product3));
		
		assertThrows(ValidationException.class, () -> productService.checkProductsStock(productsQuantity));		
	}
	
	@Test
	void shouldThrowResourceNotFoundExceptionWhenCheckProductsStockAndAnInvalidProductIdIsEncountered() {
		ProductQuantityDTO productQuantityDTO1 = new ProductQuantityDTO(VALID_PRODUCT_ID_1, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO2 = new ProductQuantityDTO(VALID_PRODUCT_ID_2, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO3 = new ProductQuantityDTO(INVALID_PRODUCT_ID, FIVE_PRODUCT_STOCK);
		List<ProductQuantityDTO> productsQuantity = List.of(productQuantityDTO1, productQuantityDTO2, productQuantityDTO3);
		Product product1 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_1, TEN_PRODUCT_STOCK);
		Product product2 = createProductWhithIdAndQuantity(VALID_PRODUCT_ID_2, TEN_PRODUCT_STOCK);
		
		when(productRepository.findById(VALID_PRODUCT_ID_1)).thenReturn(Optional.of(product1));
		when(productRepository.findById(VALID_PRODUCT_ID_2)).thenReturn(Optional.of(product2));
		when(productRepository.findById(INVALID_PRODUCT_ID)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> productService.checkProductsStock(productsQuantity));		
	}
	
	private SalesProductResponse createSalesProductResponse() {
		SalesProductResponse salesProduct = new SalesProductResponse();
		salesProduct.setStatus(200);
		String sale1Id = "f1c2956f-92b4-4d33-a2a5-1619e6547120";
		String sale2Id = "84a4793f-7163-495e-8620-11dbefbcd40d";
		String sale3Id = "1f0e574c-1fc8-40c3-bdb0-11d29ebb8002";
		salesProduct.setSalesIds(List.of(sale1Id, sale2Id, sale3Id));
		
		return salesProduct;
	}
	
	private ProductStockDTO createProductStockDTO() {
		ProductStockDTO productStockDTO = new ProductStockDTO();
		ProductQuantityDTO productQuantityDTO1 = new ProductQuantityDTO(VALID_PRODUCT_ID_1, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO2 = new ProductQuantityDTO(VALID_PRODUCT_ID_2, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO3 = new ProductQuantityDTO(VALID_PRODUCT_ID_3, FIVE_PRODUCT_STOCK);
		productStockDTO.setProducts(List.of(productQuantityDTO1, productQuantityDTO2, productQuantityDTO3));
		productStockDTO.setSalesId(SALES_ID);
		productStockDTO.setTransactionid(TRANSACTION_ID);
		
		return productStockDTO;
	}
	
	private ProductStockDTO createProductStockDTOWithInvalidId() {
		ProductStockDTO productStockDTO = new ProductStockDTO();
		ProductQuantityDTO productQuantityDTO1 = new ProductQuantityDTO(VALID_PRODUCT_ID_1, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO2 = new ProductQuantityDTO(VALID_PRODUCT_ID_2, FIVE_PRODUCT_STOCK);
		ProductQuantityDTO productQuantityDTO3 = new ProductQuantityDTO(INVALID_PRODUCT_ID, FIVE_PRODUCT_STOCK);
		productStockDTO.setProducts(List.of(productQuantityDTO1, productQuantityDTO2, productQuantityDTO3));
		productStockDTO.setSalesId(SALES_ID);
		productStockDTO.setTransactionid(TRANSACTION_ID);
		
		return productStockDTO;
	}

	private Product createProduct() {
		Product product = new Product();
		product.setName("Mouse Optico");
		product.setQuantityAvailable(10);
		Category category = new Category();
		category.setId(1);
		product.setCategory(category);
		Supplier supplier = new Supplier();
		supplier.setId(2);
		product.setSupplier(supplier);
		
		return product;
	}
	
	private Product createProductWithId(Integer id) {
		Product product = new Product();
		product.setId(id);
		product.setName("Mouse Optico");
		product.setQuantityAvailable(10);
		Category category = new Category(1, "Hardware");
		product.setCategory(category);
		Supplier supplier = new Supplier(2, "Kabum WebStore");
		product.setSupplier(supplier);
		
		return product;
	}
	
	private Product createProductWhithIdAndQuantity(Integer id, Integer quantityAvailable) {
		Product product = new Product();
		product.setId(id);
		product.setName("Mouse Optico");
		product.setQuantityAvailable(quantityAvailable);
		Category category = new Category(1, "Hardware");
		product.setCategory(category);
		Supplier supplier = new Supplier(2, "Kabum WebStore");
		product.setSupplier(supplier);
		
		return product;
	}
	
}
