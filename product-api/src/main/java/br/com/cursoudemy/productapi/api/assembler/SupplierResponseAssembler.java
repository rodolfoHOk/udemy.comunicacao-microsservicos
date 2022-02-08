package br.com.cursoudemy.productapi.api.assembler;

import java.util.List;

import org.springframework.beans.BeanUtils;

import br.com.cursoudemy.productapi.api.dto.SupplierResponse;
import br.com.cursoudemy.productapi.domain.model.Supplier;

public class SupplierResponseAssembler {

	public static SupplierResponse toModel (Supplier supplier) {
		var supplierResponse = new SupplierResponse();
		BeanUtils.copyProperties(supplier, supplierResponse);
		return supplierResponse;
	}
	
	public static List<SupplierResponse> toCollectionModel (List<Supplier> suppliers) {
		return suppliers.stream().map(supplier -> toModel(supplier)).toList();
	}
}
