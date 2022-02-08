package br.com.cursoudemy.productapi.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping
	public List<CategoryResponse> findAll () {
		return CategoryResponseAssembler.toCollectionModel(categoryService.findByAll());
	}
	
	@GetMapping("/{id}")
	public CategoryResponse findById (@PathVariable Integer id) {
		return CategoryResponseAssembler.toModel(categoryService.findById(id));
	}
	
	@GetMapping("/description/{description}")
	public List<CategoryResponse> findByDescription (@PathVariable String description) {
		return CategoryResponseAssembler.toCollectionModel(categoryService.findByDescription(description));
	}
	
}