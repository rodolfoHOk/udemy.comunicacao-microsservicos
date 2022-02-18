package br.com.cursoudemy.productapi.domain.modules.supplier.service;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.cursoudemy.productapi.domain.exception.EntityInUseException;
import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.modules.product.repository.ProductRepository;
import br.com.cursoudemy.productapi.domain.modules.supplier.model.Supplier;
import br.com.cursoudemy.productapi.domain.modules.supplier.repository.SupplierRepository;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {
	
	@Mock
	private SupplierRepository supplierRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private SupplierService supplierService;
	
	private static final Integer VALID_SUPPLIER_ID = 2;
	private static final Integer INVALID_SUPPLIER_ID = 100;
	private static final String VALID_SUPPLIER_NAME = "Amazon Webstore";
	private static final String INVALID_SUPPLIER_NAME = "Saraiva Bookstore";
	
	
	@Test
	void shouldReturnCreatedSupplierWhenSaveASuppler() {
		Supplier supplierToSave = new Supplier();
		supplierToSave.setName("Amazon WebStore");
		Supplier expectedCreateSupplier = new Supplier(2, "Amazon WebStore");
		
		when(supplierRepository.save(supplierToSave)).thenReturn(expectedCreateSupplier);
		
		Supplier createdSupplier = supplierService.save(supplierToSave);
		
		assertThat(createdSupplier.getId(), is(equalTo(expectedCreateSupplier.getId())));
		assertThat(createdSupplier.getName(), is(equalTo(expectedCreateSupplier.getName())));
	}
	
	@Test
	void shouldReturnAListOfSuppliersWhenFindAllSuppliers() {
		Supplier expectedFoundSupplier = new Supplier(2, "Amazon WebStore");
		
		when(supplierRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundSupplier));
		
		List<Supplier> findSuppliers = supplierService.findAll();
		
		assertThat(findSuppliers, is(not(empty())));
		assertThat(findSuppliers.get(0), is(equalTo(expectedFoundSupplier)));
	}
	
	@Test
	void shouldReturnAEmptyListWhenFindAllSuppliers() {
		when(supplierRepository.findAll()).thenReturn(Collections.emptyList());
		
		List<Supplier> findSuppliers = supplierService.findAll();
		
		assertThat(findSuppliers, is(empty()));
	}
	
	@Test
	void shouldReturnASupplierWhenFindSupplierByIdWithAValidId() {
		Supplier expectedFoundSupplier = new Supplier(2, "Amazon WebStore");
		
		when(supplierRepository.findById(VALID_SUPPLIER_ID)).thenReturn(Optional.of(expectedFoundSupplier));
		
		Supplier foundSupplier = supplierService.findById(VALID_SUPPLIER_ID);
		
		assertThat(foundSupplier, is(equalTo(expectedFoundSupplier)));
	}
	
	@Test
	void shouldThrowResourceNotFoundExceptionWhenFindSupplierByIdWithAInvalidId() {
		when(supplierRepository.findById(INVALID_SUPPLIER_ID)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> supplierService.findById(INVALID_SUPPLIER_ID));
	}
	
	@Test
	void shouldReturnAListOfSuppliersWhenFindSupplierByNameWithAValidName() {
		Supplier expectedFoundSupplier = new Supplier(2, "Amazon WebStore");
		
		when(supplierRepository.findByNameIgnoreCaseContaining(VALID_SUPPLIER_NAME))
			.thenReturn(Collections.singletonList(expectedFoundSupplier));
		
		List<Supplier> findSuppliers = supplierService.findByName(VALID_SUPPLIER_NAME);
		
		assertThat(findSuppliers, is(not(empty())));
		assertThat(findSuppliers.get(0), is(equalTo(expectedFoundSupplier)));
	}

	@Test
	void shouldReturnAEmptyListWhenFindSupplierByNameWithAInvalidName() {
		when(supplierRepository.findByNameIgnoreCaseContaining(INVALID_SUPPLIER_NAME))
			.thenReturn(Collections.emptyList());
		
		List<Supplier> findSuppliers = supplierService.findByName(INVALID_SUPPLIER_NAME);
		
		assertThat(findSuppliers, is(empty()));
	}
	
	@Test
	void shouldDeleteASupplierWhenDeleteSupplierWithAValidIdAndSupplierIsNotInUse() {
		when(supplierRepository.existsById(VALID_SUPPLIER_ID)).thenReturn(true);
		when(productRepository.existsBySupplierId(VALID_SUPPLIER_ID)).thenReturn(false);
		doNothing().when(supplierRepository).deleteById(VALID_SUPPLIER_ID);
		
		assertDoesNotThrow(() -> supplierService.delete(VALID_SUPPLIER_ID));
	}
	
	@Test
	void shouldThrowResourceNotFoundExceptionWhenDeleteSupplierWithAInvalidId() {
		when(supplierRepository.existsById(INVALID_SUPPLIER_ID)).thenReturn(false);
		
		assertThrows(ResourceNotFoundException.class, () -> supplierService.delete(INVALID_SUPPLIER_ID));
	}
	
	@Test
	void shouldThrowEntityInUseExceptionWhenDeleteSupplierWithAValidIdAndSupplierIsInUse() {
		when(supplierRepository.existsById(VALID_SUPPLIER_ID)).thenReturn(true);
		when(productRepository.existsBySupplierId(VALID_SUPPLIER_ID)).thenReturn(true);
		
		assertThrows(EntityInUseException.class, () -> supplierService.delete(VALID_SUPPLIER_ID));
	}
	
	@Test
	void shouldReturnAUpdatedSupplierWhenUpdateSupplierWithAValidId() {
		Supplier expectedUpdatedSupplier = new Supplier(2, "Amazon WebStore");
		
		when(supplierRepository.existsById(VALID_SUPPLIER_ID)).thenReturn(true);
		when(supplierRepository.save(expectedUpdatedSupplier)).thenReturn(expectedUpdatedSupplier);
		
		Supplier updatedSupplier = supplierService.update(expectedUpdatedSupplier, VALID_SUPPLIER_ID);
		
		assertThat(updatedSupplier, is(equalTo(expectedUpdatedSupplier)));	
	}
	
	@Test
	void shouldThrowResourceNotFoundExceptionWhenUpdateSupplierWithAInvalidId() {
		Supplier expectedUpdatedSupplier = new Supplier(2, "Amazon WebStore");
		
		when(supplierRepository.existsById(INVALID_SUPPLIER_ID)).thenReturn(false);
		
		assertThrows(ResourceNotFoundException.class, () -> supplierService.update(expectedUpdatedSupplier ,INVALID_SUPPLIER_ID));
	}
}
