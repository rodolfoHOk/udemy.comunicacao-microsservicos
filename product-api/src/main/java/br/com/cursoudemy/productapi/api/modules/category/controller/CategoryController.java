package br.com.cursoudemy.productapi.api.modules.category.controller;

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

import br.com.cursoudemy.productapi.api.dto.SuccessResponse;
import br.com.cursoudemy.productapi.api.modules.category.assembler.CategoryRequestDisassembler;
import br.com.cursoudemy.productapi.api.modules.category.assembler.CategoryResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.category.dto.CategoryRequest;
import br.com.cursoudemy.productapi.api.modules.category.dto.CategoryResponse;
import br.com.cursoudemy.productapi.api.modules.category.openapi.CategotyControllerOpenApi;
import br.com.cursoudemy.productapi.domain.modules.category.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController implements CategotyControllerOpenApi {

	@Autowired
	private CategoryService categoryService;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryResponse save (@Valid @RequestBody CategoryRequest categoryRequest) {
		var category = CategoryRequestDisassembler.toDomainObject(categoryRequest);
		return CategoryResponseAssembler.toModel(categoryService.save(category));
	}
	
	@Override
	@GetMapping
	public List<CategoryResponse> findAll () {
		return CategoryResponseAssembler.toCollectionModel(categoryService.findAll());
	}
	
	@Override
	@GetMapping("/{id}")
	public CategoryResponse findById (@PathVariable Integer id) {
		return CategoryResponseAssembler.toModel(categoryService.findById(id));
	}
	
	@Override
	@GetMapping("/description/{description}")
	public List<CategoryResponse> findByDescription (@PathVariable String description) {
		return CategoryResponseAssembler.toCollectionModel(categoryService.findByDescription(description));
	}
	
	@Override
	@DeleteMapping("/{id}")
	public SuccessResponse delete (@PathVariable Integer id) {
		categoryService.delete(id);
		return SuccessResponse.create("The category was deleted.");
	}
	
	@Override
	@PutMapping("/{id}")
	public CategoryResponse update (@PathVariable Integer id, @Valid @RequestBody CategoryRequest categoryRequest) {
		var category = CategoryRequestDisassembler.toDomainObject(categoryRequest);
		return CategoryResponseAssembler.toModel(categoryService.update(category, id));
	}
	
}
