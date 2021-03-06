package br.com.cursoudemy.productapi.domain.modules.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cursoudemy.productapi.domain.modules.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	List<Product> findByNameIgnoreCaseContaining(String name);
	
	List<Product> findByCategoryId(Integer categoryId);
	
	List<Product> findBySupplierId(Integer supplierId);
	
	Boolean existsByCategoryId(Integer categoryId);
	
	Boolean existsBySupplierId(Integer supplierId);

}
