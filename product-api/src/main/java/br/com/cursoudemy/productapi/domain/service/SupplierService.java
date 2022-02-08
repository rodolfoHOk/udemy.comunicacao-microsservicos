package br.com.cursoudemy.productapi.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import br.com.cursoudemy.productapi.domain.model.Supplier;
import br.com.cursoudemy.productapi.domain.repository.SupplierRepository;

@Service
public class SupplierService {
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Transactional
	public Supplier save (Supplier supplier) {
		validateSupplierNameInformed(supplier);
		return supplierRepository.save(supplier);
	}
	
	public List<Supplier> findAll() {
		return supplierRepository.findAll();
	}
	
	public Supplier findById (Integer id) {
		if (ObjectUtils.isEmpty(id)) {
			throw new ValidationException("The supplier id must be informed");
		}
		return supplierRepository
				.findById(id)
				.orElseThrow(() -> new ValidationException("There no supplier for the given id"));
	}
	
	public List<Supplier> findByName (String name) {
		if (ObjectUtils.isEmpty(name)) {
			throw new ValidationException("The supplier name must be informed");
		}
		return supplierRepository.findByNameIgnoreCaseContaining(name);
	}

	private void validateSupplierNameInformed(Supplier supplier) {
		if (ObjectUtils.isEmpty(supplier.getName())) {
			throw new ValidationException("The supplier name was not informed");
		}
	}
}
