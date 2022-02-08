package br.com.cursoudemy.productapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

}
