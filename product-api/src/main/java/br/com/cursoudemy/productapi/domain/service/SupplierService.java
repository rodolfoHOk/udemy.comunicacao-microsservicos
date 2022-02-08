package br.com.cursoudemy.productapi.domain.service;

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
	
	public Supplier findById (Integer id) {
		return supplierRepository
				.findById(id)
				.orElseThrow(() -> new ValidationException("There no supplier for the given id"));
	}

	private void validateSupplierNameInformed(Supplier supplier) {
		if (ObjectUtils.isEmpty(supplier.getName())) {
			throw new ValidationException("The supplier name was not informed");
		}
	}
}
