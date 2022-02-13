package br.com.cursoudemy.productapi.api.modules.supplier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursoudemy.productapi.api.dto.SuccessResponse;
import br.com.cursoudemy.productapi.api.modules.supplier.assembler.SupplierRequestDisassembler;
import br.com.cursoudemy.productapi.api.modules.supplier.assembler.SupplierResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.supplier.dto.SupplierRequest;
import br.com.cursoudemy.productapi.api.modules.supplier.dto.SupplierResponse;
import br.com.cursoudemy.productapi.domain.modules.supplier.service.SupplierService;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
	
	@Autowired
	private SupplierService supplierService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
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
	
	@DeleteMapping("/{id}")
	public SuccessResponse delete (@PathVariable Integer id) {
		supplierService.delete(id);
		return SuccessResponse.create("The supplier was deleted.");
	}
	
	@PutMapping("/{id}")
	public SupplierResponse update (@PathVariable Integer id, @RequestBody SupplierRequest supplierRequest) {
		var supplier = SupplierRequestDisassembler.toDomainObject(supplierRequest);
		return SupplierResponseAssembler.toModel(supplierService.update(supplier, id));
	}
	
}
