package br.com.cursoudemy.productapi.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import br.com.cursoudemy.productapi.domain.model.Product;
import br.com.cursoudemy.productapi.domain.repository.ProductRepository;

@Service
public class ProductService {
	
	private static final Integer ZERO = 0;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private SupplierService supplierService;
	
	@Transactional
	public Product save (Product product) {
		validateProductDataInformed(product);
		var category = categoryService.findById(product.getCategory().getId());
		var supplier = supplierService.findById(product.getSupplier().getId());
		product.setCategory(category);
		product.setSupplier(supplier);
		return productRepository.save(product);
	}

	private void validateProductDataInformed(Product product) {
		if (ObjectUtils.isEmpty(product.getName())) {
			throw new ValidationException("The product name was not informed");
		}
		if (ObjectUtils.isEmpty(product.getQuantityAvailable())) {
			throw new ValidationException("The product quantity was not informed");
		}
		if (product.getQuantityAvailable() <= ZERO) {
			throw new ValidationException("The product quantity should not be less or equal to zero");
		}
		if (ObjectUtils.isEmpty(product.getCategory().getId())) {
			throw new ValidationException("The product category id was not informed");
		}
		if (ObjectUtils.isEmpty(product.getSupplier().getId())) {
			throw new ValidationException("The product supplier id was not informed");
		}
	}

}
