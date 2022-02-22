package br.com.cursoudemy.productapi.api.modules.supplier.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import br.com.cursoudemy.productapi.api.modules.supplier.dto.SupplierResponse;
import br.com.cursoudemy.productapi.domain.modules.supplier.model.Supplier;

public class SupplierResponseAssembler {

	public static SupplierResponse toModel (Supplier supplier) {
		var supplierResponse = new SupplierResponse();
		BeanUtils.copyProperties(supplier, supplierResponse);
		return supplierResponse;
	}
	
	public static List<SupplierResponse> toCollectionModel (List<Supplier> suppliers) {
		return suppliers.stream().map(supplier -> toModel(supplier)).collect(Collectors.toList());
	}
}
