package br.com.cursoudemy.productapi.api.controller;

import java.util.List;

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

import br.com.cursoudemy.productapi.api.assembler.ProductRequestDisassembler;
import br.com.cursoudemy.productapi.api.assembler.ProductResponseAssembler;
import br.com.cursoudemy.productapi.api.assembler.ProductSalesResponseAssembler;
import br.com.cursoudemy.productapi.api.dto.ProductCheckStockRequest;
import br.com.cursoudemy.productapi.api.dto.ProductRequest;
import br.com.cursoudemy.productapi.api.dto.ProductResponse;
import br.com.cursoudemy.productapi.api.dto.ProductSalesResponse;
import br.com.cursoudemy.productapi.api.dto.SuccessResponse;
import br.com.cursoudemy.productapi.domain.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse save (@RequestBody ProductRequest productRequest) {
		var product = ProductRequestDisassembler.toDomainObject(productRequest);
		
		return ProductResponseAssembler.toModel(productService.save(product));
	}
	
	@GetMapping
	public List<ProductResponse> findAll() {
		return ProductResponseAssembler.toCollectionModel(productService.findAll());
	}
	
	@GetMapping("/{id}")
	public ProductResponse findById (@PathVariable Integer id) {
		return ProductResponseAssembler.toModel(productService.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public List<ProductResponse> findByCategoryId (@PathVariable String name) {
		return ProductResponseAssembler.toCollectionModel(productService.findByName(name));
	}
	
	@GetMapping("/category/{categoryId}")
	public List<ProductResponse> findByCategoryId (@PathVariable Integer categoryId) {
		return ProductResponseAssembler.toCollectionModel(productService.findByCategoryId(categoryId));
	}
	
	@GetMapping("/supplier/{supplierId}")
	public List<ProductResponse> findBySupplierId (@PathVariable Integer supplierId) {
		return ProductResponseAssembler.toCollectionModel(productService.findBySupplierId(supplierId));
	}

	@DeleteMapping("/{id}")
	public SuccessResponse delete (@PathVariable Integer id) {
		productService.delete(id);
		return SuccessResponse.create("The product was deleted.");
	}
	
	@PutMapping("/{id}")
	public ProductResponse update (@PathVariable Integer id, @RequestBody ProductRequest productRequest) {
		var product = ProductRequestDisassembler.toDomainObject(productRequest);
		return ProductResponseAssembler.toModel(productService.update(product, id));
	}
	
	@GetMapping("/{id}/sales")
	public ProductSalesResponse findProductSales(@PathVariable Integer id) {
		return ProductSalesResponseAssembler.toModel(productService.findProductSales(id));
	}
	
	@PostMapping("/check-stock")
	public SuccessResponse checkProductsStock(@RequestBody ProductCheckStockRequest productCheckStockRequest) {
		productService.checkProductsStock(productCheckStockRequest.getProducts());
		return SuccessResponse.create("The stock is ok");
	}
	
}
