package br.com.cursoudemy.productapi.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import br.com.cursoudemy.productapi.domain.model.Category;
import br.com.cursoudemy.productapi.domain.repository.CategoryRepository;
import br.com.cursoudemy.productapi.domain.repository.ProductRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Transactional
	public Category save (Category category) {
		validateCategoryNameInformed(category);
		return categoryRepository.save(category);
	}
	
	public List<Category> findByAll () {
		return categoryRepository.findAll();
	}
		
	public Category findById (Integer id) {
		validateInformedId(id);
		return categoryRepository
				.findById(id)
				.orElseThrow(() -> new ValidationException("There no category for the given id"));
	}
	
	public List<Category> findByDescription (String description) {
		if (ObjectUtils.isEmpty(description)) {
			throw new ValidationException("The category description must be informed");
		}
		return categoryRepository.findByDescriptionIgnoreCaseContaining(description);
	}
	
	@Transactional
	public void delete (Integer id) {
		validateInformedId(id);
		if (productRepository.existsByCategoryId(id)) {
			throw new ValidationException("You cannot delete this category because it is already defined by a product");
		}
		try {
			categoryRepository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new ValidationException("Not exist category with id " + id);
		}
	}
	
	@Transactional
	public Category update (Category category, Integer id) {
		validateInformedId(id);
		validateCategoryNameInformed(category);
		findById(id);
		category.setId(id);
		return categoryRepository.save(category);
	}
	
	private void validateInformedId (Integer id) {
		if (ObjectUtils.isEmpty(id)) {
			throw new ValidationException("The category id must be informed");
		}
	}
	
	private void validateCategoryNameInformed(Category category) {
		if (ObjectUtils.isEmpty(category.getDescription())) {
			throw new ValidationException("The category description was not informed");
		}
	}
	
}
