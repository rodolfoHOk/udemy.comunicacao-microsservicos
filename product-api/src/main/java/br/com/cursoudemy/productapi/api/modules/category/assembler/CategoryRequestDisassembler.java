package br.com.cursoudemy.productapi.api.modules.category.assembler;

import org.springframework.beans.BeanUtils;

import br.com.cursoudemy.productapi.api.modules.category.dto.CategoryRequest;
import br.com.cursoudemy.productapi.domain.modules.category.model.Category;

public class CategoryRequestDisassembler {
	
	public static Category toDomainObject(CategoryRequest categoryRequest) {
		var category = new Category();
		BeanUtils.copyProperties(categoryRequest, category);
		return category;
	}
}
