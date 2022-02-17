package br.com.cursoudemy.productapi.domain.modules.supplier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cursoudemy.productapi.domain.exception.EntityInUseException;
import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import br.com.cursoudemy.productapi.domain.modules.product.repository.ProductRepository;
import br.com.cursoudemy.productapi.domain.modules.supplier.model.Supplier;
import br.com.cursoudemy.productapi.domain.modules.supplier.repository.SupplierRepository;

@Service
public class SupplierService {
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Transactional
	public Supplier save (Supplier supplier) {
		return supplierRepository.save(supplier);
	}
	
	public List<Supplier> findAll() {
		return supplierRepository.findAll();
	}
	
	public Supplier findById (Integer id) {
		return supplierRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("There no supplier for the given id"));
	}
	
	public List<Supplier> findByName (String name) {
		return supplierRepository.findByNameIgnoreCaseContaining(name);
	}
	
	@Transactional
	public void delete (Integer id) {
		validateExistById(id);
		if (productRepository.existsBySupplierId(id)) {
			throw new EntityInUseException("You cannot delete this supplier because it is already defined by a product");
		}
		supplierRepository.deleteById(id);
	}
	
	@Transactional
	public Supplier update (Supplier supplier, Integer id) {
		validateExistById(id);
		supplier.setId(id);
		return supplierRepository.save(supplier);
	}
	
	private void validateExistById (Integer id) {
		if (!supplierRepository.existsById(id)) {
			throw new ValidationException("Not exist supplier with id " + id);
		}
	}
	
}
