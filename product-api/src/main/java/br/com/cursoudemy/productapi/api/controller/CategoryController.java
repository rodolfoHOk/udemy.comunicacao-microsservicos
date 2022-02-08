package br.com.cursoudemy.productapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursoudemy.productapi.api.assembler.CategoryRequestDisassembler;
import br.com.cursoudemy.productapi.api.assembler.CategoryResponseAssembler;
import br.com.cursoudemy.productapi.api.dto.CategoryRequest;
import br.com.cursoudemy.productapi.api.dto.CategoryResponse;
import br.com.cursoudemy.productapi.domain.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@PostMapping
	public CategoryResponse save (@RequestBody CategoryRequest categoryRequest) {
		var category = CategoryRequestDisassembler.toDomainObject(categoryRequest);
		
		return CategoryResponseAssembler.toModel(categoryService.save(category));
	}
}
