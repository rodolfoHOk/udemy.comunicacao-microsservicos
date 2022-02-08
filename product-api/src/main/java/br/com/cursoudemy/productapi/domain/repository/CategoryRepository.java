package br.com.cursoudemy.productapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cursoudemy.productapi.domain.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
