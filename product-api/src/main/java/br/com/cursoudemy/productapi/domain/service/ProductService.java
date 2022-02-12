package br.com.cursoudemy.productapi.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import br.com.cursoudemy.productapi.core.interceptor.RequestUtils;
import br.com.cursoudemy.productapi.domain.client.SalesClient;
import br.com.cursoudemy.productapi.domain.client.dto.SalesProductResponse;
import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import br.com.cursoudemy.productapi.domain.listener.dto.ProductQuantityDTO;
import br.com.cursoudemy.productapi.domain.listener.dto.ProductStockDTO;
import br.com.cursoudemy.productapi.domain.model.Product;
import br.com.cursoudemy.productapi.domain.model.dto.ProductSales;
import br.com.cursoudemy.productapi.domain.repository.ProductRepository;
import br.com.cursoudemy.productapi.domain.sender.SalesConfirmationSender;
import br.com.cursoudemy.productapi.domain.sender.dto.SalesConfirmationDTO;
import br.com.cursoudemy.productapi.domain.sender.enums.SalesStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {
	
	private static final Integer ZERO = 0;
	private static final String TRANSACTION_ID = "transactionid";
	private static final String SERVICE_ID = "serviceid";
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private SalesConfirmationSender salesConfirmationSender;
	
	@Autowired
	private SalesClient salesClient;
	
	@Transactional
	public Product save (Product product) {
		validateProductDataInformed(product);
		var category = categoryService.findById(product.getCategory().getId());
		var supplier = supplierService.findById(product.getSupplier().getId());
		product.setCategory(category);
		product.setSupplier(supplier);
		return productRepository.save(product);
	}
	
	public List<Product> findAll() {
		return productRepository.findAll();
	}
	
	public Product findById (Integer id) {
		validateInformedId(id);
		return productRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("There no product for the given id"));
	}
	
	public List<Product> findByName (String name) {
		if (ObjectUtils.isEmpty(name)) {
			throw new ValidationException("The product name must be informed");
		}
		return productRepository.findByNameIgnoreCaseContaining(name);
	}
	
	public List<Product> findByCategoryId (Integer categoryId) {
		if (ObjectUtils.isEmpty(categoryId)) {
			throw new ValidationException("The product category Id must be informed");
		}
		return productRepository.findByCategoryId(categoryId);
	}
	
	public List<Product> findBySupplierId (Integer supplierId) {
		if (ObjectUtils.isEmpty(supplierId)) {
			throw new ValidationException("The product supplier Id must be informed");
		}
		return productRepository.findBySupplierId(supplierId);
	}
	
	@Transactional
	public void delete (Integer id) {
		validateInformedId(id);
		validateExistById(id);
		productRepository.deleteById(id);
	}
	
	@Transactional
	public Product update (Product product, Integer id) {
		validateInformedId(id);
		validateProductDataInformed(product);
		validateExistById(id);
		var category = categoryService.findById(product.getCategory().getId());
		var supplier = supplierService.findById(product.getSupplier().getId());
		product.setCategory(category);
		product.setSupplier(supplier);
		product.setId(id);
		return productRepository.save(product);
	}
	
	@Transactional
	public void updateProductStock(ProductStockDTO productStockDTO) {
		try {
			validateStockUpdateData(productStockDTO);
			productStockDTO.getProducts().forEach(this::validateProductStock);
			productStockDTO.getProducts().forEach(salesProduct -> {
				var existingProduct = findById(salesProduct.getProductId());
				existingProduct.updateStock(salesProduct.getQuantity());
			});
			log.info("Stock updated successfully | Transaction ID: {}", productStockDTO.getTransactionid());
			var approvedMessage = new SalesConfirmationDTO(
					productStockDTO.getSalesId(), SalesStatus.APPROVED, productStockDTO.getTransactionid());
			salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
		} catch (Exception ex) {
			if (ex instanceof ValidationException) {
				log.error("Error while trying to update stock for message: {} and TransactionId: {}",
						ex.getMessage(), productStockDTO.getTransactionid());
			} else {
				log.error("Error while trying to update stock for message: {} and TransactionId: {}",
						ex.getMessage(), productStockDTO.getTransactionid(), ex);
			}
			var rejectedMessage = new SalesConfirmationDTO(
					productStockDTO.getSalesId(), SalesStatus.REJECTED, productStockDTO.getTransactionid());
			salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);
		}
	}
	
	public ProductSales findProductSales(Integer id) {
		var product = findById(id);
		var sales = getSalesByProductId(product.getId());
		return ProductSales.of(product, sales.getSalesIds());
	}
	
	public void checkProductsStock(List<ProductQuantityDTO> productsQuantity) {
		if (ObjectUtils.isEmpty(productsQuantity)) {
			throw new ValidationException("The products list must not be empty");
		}
		productsQuantity.forEach(this::validateProductStock);
	}
	
	private void validateProductStock(ProductQuantityDTO productsQuantity) {
		if (ObjectUtils.isEmpty(productsQuantity.getProductId()) 
				|| ObjectUtils.isEmpty(productsQuantity.getQuantity())) {
			throw new ValidationException("Product Id and quantity must be informed");
		}
		var product = findById(productsQuantity.getProductId());
		if (productsQuantity.getQuantity() > product.getQuantityAvailable()) {
			throw new ValidationException(String.format("The product %s is out of stock", product.getId()));
		}
	}
	
	private void validateStockUpdateData(ProductStockDTO productStockDTO) {
		if (ObjectUtils.isEmpty(productStockDTO) || ObjectUtils.isEmpty(productStockDTO.getSalesId())) {
			throw new ValidationException("The product data and sales ID must be informed");
		}
		if (ObjectUtils.isEmpty(productStockDTO.getProducts())) {
			throw new ValidationException("The sales products must be informed");
		}
	}
	
	private SalesProductResponse getSalesByProductId(Integer productId) {
		var currentRequest = RequestUtils.getCurrentRequest();
		var transactionid = currentRequest.getHeader(TRANSACTION_ID);
		var serviceid = currentRequest.getAttribute(SERVICE_ID);
		log.info("Sending GET request to orders by product ID with data {} | [TransactionID: {} | ServiceID: {}]",
				productId, transactionid, serviceid);
		var sales = salesClient.findSalesByProductId(productId)
				.orElseThrow(() -> new ResourceNotFoundException("The sales was not found by this product"));
		log.info("Receiving GET request to orders by product ID with data {} | [TransactionID: {} | ServiceID: {}]",
				sales.toString(), transactionid, serviceid);
		return sales;
	}
	
	private void validateInformedId (Integer id) {
		if (ObjectUtils.isEmpty(id)) {
			throw new ValidationException("The product id must be informed");
		}
	}

	private void validateProductDataInformed(Product product) {
		if (ObjectUtils.isEmpty(product.getName())) {
			throw new ValidationException("The product name was not informed");
		}
		if (ObjectUtils.isEmpty(product.getQuantityAvailable())) {
			throw new ValidationException("The product quantity was not informed");
		}
		if (product.getQuantityAvailable() <= ZERO) {
			throw new ValidationException("The product quantity should not be less or equal to zero");
		}
		if (ObjectUtils.isEmpty(product.getCategory().getId())) {
			throw new ValidationException("The product category id was not informed");
		}
		if (ObjectUtils.isEmpty(product.getSupplier().getId())) {
			throw new ValidationException("The product supplier id was not informed");
		}
	}
	
	private void validateExistById (Integer id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Not exist product with id " + id);
		}
	}

}
