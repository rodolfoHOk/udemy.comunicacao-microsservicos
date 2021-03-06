package br.com.cursoudemy.productapi.domain.modules.product.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import br.com.cursoudemy.productapi.domain.modules.category.model.Category;
import br.com.cursoudemy.productapi.domain.modules.supplier.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name = "NAME", nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name="FK_CATEGORY", nullable = false)
	private Category category;
	
	@ManyToOne
	@JoinColumn(name="FK_SUPPLIER", nullable = false)
	private Supplier supplier; 
	
	@Column(name = "QUANTITY_AVAILABLE", nullable = false)
	private Integer quantityAvailable;
	
	@Column(name = "CREATED_AT", nullable = false, updatable = false)
	private OffsetDateTime createdAt;
	
	@PrePersist
	public void prePersist() {
		createdAt = OffsetDateTime.now();
	}
	
	public void updateStock(Integer quantity) {
		quantityAvailable = quantityAvailable - quantity;
	}
	
}
