package br.com.cursoudemy.productapi.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping
	public List<SupplierResponse> findAll() {
		return SupplierResponseAssembler.toCollectionModel(supplierService.findAll());
	}
	
	@GetMapping("/{id}")
	public SupplierResponse findById (@PathVariable Integer id) {
		return SupplierResponseAssembler.toModel(supplierService.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public List<SupplierResponse> findByName (@PathVariable String name) {
		return SupplierResponseAssembler.toCollectionModel(supplierService.findByName(name));
	}
	
}
