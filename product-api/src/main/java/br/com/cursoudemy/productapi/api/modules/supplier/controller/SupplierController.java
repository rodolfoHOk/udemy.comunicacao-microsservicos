package br.com.cursoudemy.productapi.api.modules.supplier.controller;

import java.util.List;

import javax.validation.Valid;

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
import br.com.cursoudemy.productapi.api.modules.supplier.openapi.SupplierControllerOpenApi;
import br.com.cursoudemy.productapi.domain.modules.supplier.service.SupplierService;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController implements SupplierControllerOpenApi {
	
	@Autowired
	private SupplierService supplierService;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SupplierResponse save (@Valid @RequestBody SupplierRequest supplierRequest) {
		var supplier = SupplierRequestDisassembler.toDomainObject(supplierRequest);
		return SupplierResponseAssembler.toModel(supplierService.save(supplier));
	}
	
	@Override
	@GetMapping
	public List<SupplierResponse> findAll() {
		return SupplierResponseAssembler.toCollectionModel(supplierService.findAll());
	}
	
	@Override
	@GetMapping("/{id}")
	public SupplierResponse findById (@PathVariable Integer id) {
		return SupplierResponseAssembler.toModel(supplierService.findById(id));
	}
	
	@Override
	@GetMapping("/name/{name}")
	public List<SupplierResponse> findByName (@PathVariable String name) {
		return SupplierResponseAssembler.toCollectionModel(supplierService.findByName(name));
	}
	
	@Override
	@DeleteMapping("/{id}")
	public SuccessResponse delete (@PathVariable Integer id) {
		supplierService.delete(id);
		return SuccessResponse.create("The supplier was deleted.");
	}
	
	@Override
	@PutMapping("/{id}")
	public SupplierResponse update (@PathVariable Integer id, @Valid @RequestBody SupplierRequest supplierRequest) {
		var supplier = SupplierRequestDisassembler.toDomainObject(supplierRequest);
		return SupplierResponseAssembler.toModel(supplierService.update(supplier, id));
	}
	
}
