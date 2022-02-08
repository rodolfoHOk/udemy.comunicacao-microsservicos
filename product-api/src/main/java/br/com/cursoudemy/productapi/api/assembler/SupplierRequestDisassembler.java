package br.com.cursoudemy.productapi.api.assembler;

import org.springframework.beans.BeanUtils;

import br.com.cursoudemy.productapi.api.dto.SupplierRequest;
import br.com.cursoudemy.productapi.domain.model.Supplier;

public class SupplierRequestDisassembler {
	
	public static Supplier toDomainObject (SupplierRequest supplierRequest) {
		var supplier = new Supplier();
		BeanUtils.copyProperties(supplierRequest, supplier);
		return supplier;
	}
	
}
