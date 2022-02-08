package br.com.cursoudemy.productapi.api.assembler;

import org.springframework.beans.BeanUtils;

import br.com.cursoudemy.productapi.api.dto.CategoryRequest;
import br.com.cursoudemy.productapi.domain.model.Category;

public class CategoryRequestDisassembler {
	
	public static Category toDomainObject(CategoryRequest categoryRequest) {
		var category = new Category();
		BeanUtils.copyProperties(categoryRequest, category);
		return category;
	}
}
