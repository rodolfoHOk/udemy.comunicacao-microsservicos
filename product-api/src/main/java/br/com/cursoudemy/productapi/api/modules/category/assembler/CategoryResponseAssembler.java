package br.com.cursoudemy.productapi.api.modules.category.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import br.com.cursoudemy.productapi.api.modules.category.dto.CategoryResponse;
import br.com.cursoudemy.productapi.domain.modules.category.model.Category;

public class CategoryResponseAssembler {
	
	public static CategoryResponse toModel (Category category) {
		var categoryResponse = new CategoryResponse();
		BeanUtils.copyProperties(category, categoryResponse);
		return categoryResponse;
	}
	
	public static List<CategoryResponse> toCollectionModel (List<Category> categories) {
		return categories.stream().map(category -> toModel(category)).collect(Collectors.toList());
	}
	
}
