package br.com.cursoudemy.productapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursoudemy.productapi.api.assembler.SupplierRequestDisassembler;
import br.com.cursoudemy.productapi.api.assembler.SupplierResponseAssembler;
import br.com.cursoudemy.productapi.api.dto.SupplierRequest;
import br.com.cursoudemy.productapi.api.dto.SupplierResponse;
import br.com.cursoudemy.productapi.domain.service.SupplierService;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
	
	@Autowired
	private SupplierService supplierService;
	
	@PostMapping
	public SupplierResponse save (@RequestBody SupplierRequest supplierRequest) {
		var supplier = SupplierRequestDisassembler.toDomainObject(supplierRequest);
		return SupplierResponseAssembler.toModel(supplierService.save(supplier));
	}
	
}
