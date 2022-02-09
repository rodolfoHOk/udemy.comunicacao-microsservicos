package br.com.cursoudemy.productapi.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
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
	
	public List<Product> findAll() {
		return productRepository.findAll();
	}
	
	public Product findById (Integer id) {
		validateInformedId(id);
		return productRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("There no product for the given id"));
	}
	
	public List<Product> findByName (String name) {
		if (ObjectUtils.isEmpty(name)) {
			throw new ValidationException("The product name must be informed");
		}
		return productRepository.findByNameIgnoreCaseContaining(name);
	}
	
	public List<Product> findByCategoryId (Integer categoryId) {
		if (ObjectUtils.isEmpty(categoryId)) {
			throw new ValidationException("The product category Id must be informed");
		}
		return productRepository.findByCategoryId(categoryId);
	}
	
	public List<Product> findBySupplierId (Integer supplierId) {
		if (ObjectUtils.isEmpty(supplierId)) {
			throw new ValidationException("The product supplier Id must be informed");
		}
		return productRepository.findBySupplierId(supplierId);
	}
	
	@Transactional
	public void delete (Integer id) {
		validateInformedId(id);
		validateExistById(id);
		productRepository.deleteById(id);
	}
	
	@Transactional
	public Product update (Product product, Integer id) {
		validateInformedId(id);
		validateProductDataInformed(product);
		validateExistById(id);
		var category = categoryService.findById(product.getCategory().getId());
		var supplier = supplierService.findById(product.getSupplier().getId());
		product.setCategory(category);
		product.setSupplier(supplier);
		product.setId(id);
		return productRepository.save(product);
	}
	
	private void validateInformedId (Integer id) {
		if (ObjectUtils.isEmpty(id)) {
			throw new ValidationException("The product id must be informed");
		}
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
	
	private void validateExistById (Integer id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Not exist product with id " + id);
		}
	}

}
