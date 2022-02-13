package br.com.cursoudemy.productapi.domain.modules.supplier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cursoudemy.productapi.domain.modules.supplier.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{

	List<Supplier> findByNameIgnoreCaseContaining(String name);
	
}
