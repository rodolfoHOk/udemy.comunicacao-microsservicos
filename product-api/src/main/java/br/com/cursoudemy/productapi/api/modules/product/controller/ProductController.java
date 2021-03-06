package br.com.cursoudemy.productapi.api.modules.product.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import br.com.cursoudemy.productapi.api.dto.SuccessResponse;
import br.com.cursoudemy.productapi.api.modules.product.assembler.ProductRequestDisassembler;
import br.com.cursoudemy.productapi.api.modules.product.assembler.ProductResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.product.assembler.ProductSalesResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductCheckStockRequest;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductRequest;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductResponse;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductSalesResponse;
import br.com.cursoudemy.productapi.api.modules.product.openapi.ProductControllerOpenApi;
import br.com.cursoudemy.productapi.domain.modules.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController implements ProductControllerOpenApi {
	
	private static final String TRANSACTION_ID = "transactionid";
	private static final String SERVICE_ID = "serviceid";
	
	@Autowired
	private ProductService productService;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse save (@Valid @RequestBody ProductRequest productRequest) {
		var product = ProductRequestDisassembler.toDomainObject(productRequest);
		
		return ProductResponseAssembler.toModel(productService.save(product));
	}
	
	@Override
	@GetMapping
	public List<ProductResponse> findAll() {
		return ProductResponseAssembler.toCollectionModel(productService.findAll());
	}
	
	@Override
	@GetMapping("/{id}")
	public ProductResponse findById (@PathVariable Integer id) {
		return ProductResponseAssembler.toModel(productService.findById(id));
	}
	
	@Override
	@GetMapping("/name/{name}")
	public List<ProductResponse> findByName (@PathVariable String name) {
		return ProductResponseAssembler.toCollectionModel(productService.findByName(name));
	}
	
	@Override
	@GetMapping("/category/{categoryId}")
	public List<ProductResponse> findByCategoryId (@PathVariable Integer categoryId) {
		return ProductResponseAssembler.toCollectionModel(productService.findByCategoryId(categoryId));
	}
	
	@Override
	@GetMapping("/supplier/{supplierId}")
	public List<ProductResponse> findBySupplierId (@PathVariable Integer supplierId) {
		return ProductResponseAssembler.toCollectionModel(productService.findBySupplierId(supplierId));
	}

	@Override
	@DeleteMapping("/{id}")
	public SuccessResponse delete (@PathVariable Integer id) {
		productService.delete(id);
		return SuccessResponse.create("The product was deleted.");
	}
	
	@Override
	@PutMapping("/{id}")
	public ProductResponse update (@PathVariable Integer id, @Valid @RequestBody ProductRequest productRequest) {
		var product = ProductRequestDisassembler.toDomainObject(productRequest);
		return ProductResponseAssembler.toModel(productService.update(product, id));
	}
	
	@Override
	@GetMapping("/{id}/sales")
	public ProductSalesResponse findProductSales(@PathVariable Integer id) {
		return ProductSalesResponseAssembler.toModel(productService.findProductSales(id));
	}
	
	@Override
	@PostMapping("/check-stock")
	public SuccessResponse checkProductsStock(@Valid @RequestBody ProductCheckStockRequest productCheckStockRequest,
			WebRequest request) {
		var transactionid = request.getHeader(TRANSACTION_ID);
		var serviceid = request.getAttribute(SERVICE_ID, 0);
		log.info("Request to POST product stock with data {} | [TransactionID: {} | ServiceID: {}]",
					productCheckStockRequest.toString(), transactionid, serviceid);
		productService.checkProductsStock(productCheckStockRequest.getProducts());
		var response = SuccessResponse.create("The stock is ok");
		log.info("Response to POST product stock with data {} | [TransactionID: {} | ServiceID: {}]",
				response.toString(), transactionid, serviceid);
		return response;
	}
	
}
