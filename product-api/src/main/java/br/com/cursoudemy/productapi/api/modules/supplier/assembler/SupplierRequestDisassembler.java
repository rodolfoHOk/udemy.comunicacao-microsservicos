package br.com.cursoudemy.productapi.api.modules.supplier.assembler;

import org.springframework.beans.BeanUtils;

import br.com.cursoudemy.productapi.api.modules.supplier.dto.SupplierRequest;
import br.com.cursoudemy.productapi.domain.modules.supplier.model.Supplier;

public class SupplierRequestDisassembler {
	
	public static Supplier toDomainObject (SupplierRequest supplierRequest) {
		var supplier = new Supplier();
		BeanUtils.copyProperties(supplierRequest, supplier);
		return supplier;
	}
	
}
