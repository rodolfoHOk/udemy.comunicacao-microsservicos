package br.com.cursoudemy.productapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cursoudemy.productapi.domain.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{

}
