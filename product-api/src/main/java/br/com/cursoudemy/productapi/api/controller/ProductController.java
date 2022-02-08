package br.com.cursoudemy.productapi.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursoudemy.productapi.api.assembler.ProductRequestDisassembler;
import br.com.cursoudemy.productapi.api.assembler.ProductResponseAssembler;
import br.com.cursoudemy.productapi.api.dto.ProductRequest;
import br.com.cursoudemy.productapi.api.dto.ProductResponse;
import br.com.cursoudemy.productapi.domain.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping
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

}