package br.com.cursoudemy.productapi.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import br.com.cursoudemy.productapi.domain.model.Category;
import br.com.cursoudemy.productapi.domain.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional
	public Category save (Category category) {
		validateCategoryNameInformed(category);
		return categoryRepository.save(category);
	}
	
	private void validateCategoryNameInformed(Category category) {
		if (ObjectUtils.isEmpty(category.getDescription())) {
			throw new ValidationException("The category description was not informed");
		}
	}
}
