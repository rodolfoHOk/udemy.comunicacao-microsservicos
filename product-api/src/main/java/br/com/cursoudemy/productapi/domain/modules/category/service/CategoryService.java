package br.com.cursoudemy.productapi.domain.modules.category.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cursoudemy.productapi.domain.exception.EntityInUseException;
import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.modules.category.model.Category;
import br.com.cursoudemy.productapi.domain.modules.category.repository.CategoryRepository;
import br.com.cursoudemy.productapi.domain.modules.product.repository.ProductRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Transactional
	public Category save (Category category) {
		return categoryRepository.save(category);
	}
	
	public List<Category> findAll () {
		return categoryRepository.findAll();
	}
		
	public Category findById (Integer id) {
		return categoryRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("There no category for the given id"));
	}
	
	public List<Category> findByDescription (String description) {
		return categoryRepository.findByDescriptionIgnoreCaseContaining(description);
	}
	
	@Transactional
	public void delete (Integer id) {
		validateExistById(id);
		if (productRepository.existsByCategoryId(id)) {
			throw new EntityInUseException("You cannot delete this category because it is already defined by a product");
		}
		categoryRepository.deleteById(id);
	}
	
	@Transactional
	public Category update (Category category, Integer id) {
		validateExistById(id);
		category.setId(id);
		return categoryRepository.save(category);
	}
	
	private void validateExistById (Integer id) {
		if (!categoryRepository.existsById(id)) {
			throw new ResourceNotFoundException("Not exist category with id " + id);
		}
	}
	
}
