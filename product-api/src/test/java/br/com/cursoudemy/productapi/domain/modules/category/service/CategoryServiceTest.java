package br.com.cursoudemy.productapi.domain.modules.category.service;

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
import br.com.cursoudemy.productapi.domain.exception.ValidationException;
import br.com.cursoudemy.productapi.domain.modules.category.model.Category;
import br.com.cursoudemy.productapi.domain.modules.category.repository.CategoryRepository;
import br.com.cursoudemy.productapi.domain.modules.product.repository.ProductRepository;
import br.com.cursoudemy.productapi.domain.modules.product.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private CategoryService categoryService;
	
	@InjectMocks
	private ProductService productService;
	
	private static final Integer VALID_CATEGORY_ID = 1;
	private static final Integer INVALID_CATEGORY_ID = 100;
	private static final String VALID_CATEGORY_DESCRIPTION = "movies";
	private static final String INVALID_CATEGORY_DESCRIPTION = "fruits";
	
	@Test
	void shouldReturnCreatedCategoryWhenSaveACategory() {
		// given
		Category category = new Category(1, "Books");
		Category expectedCreatedCategory = new Category(1, "Books");
		
		// when
		when(categoryRepository.save(category)).thenReturn(expectedCreatedCategory);
		
		// then
		Category createdCategory = categoryService.save(category);
		
		assertThat(createdCategory.getId(), is(equalTo(category.getId())));
		assertThat(createdCategory.getDescription(), is(equalTo(category.getDescription())));
	}
	
	@Test
	void shouldReturnListOfCategoryWhenFindAllCategories() {
		Category expectedFoundCategory = new Category(1, "Books");
		
		when(categoryRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundCategory));
		
		List<Category> foundCategories = categoryService.findAll();
		
		assertThat(foundCategories, is(not(empty())));
		assertThat(foundCategories.get(0), is(equalTo(expectedFoundCategory)));
	}
	
	@Test
	void shouldReturnEmptyListWhenFindAllCategories() {
		when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
		
		List<Category> foundCategories = categoryService.findAll();
		
		assertThat(foundCategories, is(empty()));
	}
	
	@Test
	void shouldReturnACategoryWhenFindCategoryByIdWithAValidId() {
		Category expectedFoundCategory = new Category(1, "Books");
		
		when(categoryRepository.findById(VALID_CATEGORY_ID)).thenReturn(Optional.of(expectedFoundCategory));
		
		Category foundCategory = categoryService.findById(VALID_CATEGORY_ID);
		
		assertThat(foundCategory, is(equalTo(expectedFoundCategory)));	
	}
	
	@Test
	void shouldThrowResourceNotFoundExceptionWhenFindCategoryByIdWithAInvalidId() {
		when(categoryRepository.findById(INVALID_CATEGORY_ID)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(INVALID_CATEGORY_ID));	
	}
	
	@Test
	void shouldReturnListOfCategoriesWhenFindCategoryByDescriptionWithAValidDescription() {
		Category expectedFoundCategory = new Category(2, "Movies");
		
		when(categoryRepository.findByDescriptionIgnoreCaseContaining(VALID_CATEGORY_DESCRIPTION))
			.thenReturn(Collections.singletonList(expectedFoundCategory));
		
		List<Category> foundCategories = categoryService.findByDescription(VALID_CATEGORY_DESCRIPTION);
		
		assertThat(foundCategories, is(not(empty())));
		assertThat(foundCategories.get(0), is(equalTo(expectedFoundCategory)));
	}
	
	@Test
	void shouldReturnAEmptyListWhenFindCategoryByDescriptionWithAInvalidDescription() {
		when(categoryRepository.findByDescriptionIgnoreCaseContaining(INVALID_CATEGORY_DESCRIPTION))
			.thenReturn(Collections.emptyList());
		
		List<Category> foundCategories = categoryService.findByDescription(INVALID_CATEGORY_DESCRIPTION);
		
		assertThat(foundCategories, is(empty()));
	}
	
	@Test
	void shouldDeleteACategoryWhenDeleteCategoryWithAValidIdAndCategoryIsNotInUse() {
		when(productRepository.existsByCategoryId(VALID_CATEGORY_ID)).thenReturn(false);
		when(categoryRepository.existsById(VALID_CATEGORY_ID)).thenReturn(true);
		doNothing().when(categoryRepository).deleteById(VALID_CATEGORY_ID);
		
		assertDoesNotThrow(() -> categoryService.delete(VALID_CATEGORY_ID));
	}
	
	@Test
	void shouldThrowValidationExceptionWhenDeleteCategoryWithAInvalidId() {
		when(categoryRepository.existsById(INVALID_CATEGORY_ID)).thenReturn(false);
		
		assertThrows(ValidationException.class, () -> categoryService.delete(INVALID_CATEGORY_ID));
	}
	
	@Test
	void shouldThrowEntityInUseExceptionWhenDeleteCategoryWithAValidIdAndCategoryIsInUse() {
		when(productRepository.existsByCategoryId(VALID_CATEGORY_ID)).thenReturn(true);
		when(categoryRepository.existsById(VALID_CATEGORY_ID)).thenReturn(true);
		
		assertThrows(EntityInUseException.class, () -> categoryService.delete(VALID_CATEGORY_ID));
	}
	
	@Test
	void shouldUpdateACategoryWhenUpdateCategoryWithAValidId() {
		Category expectedUpdatedCategory = new Category(1, "Books");
		
		when(categoryRepository.existsById(VALID_CATEGORY_ID)).thenReturn(true);
		when(categoryRepository.save(expectedUpdatedCategory)).thenReturn(expectedUpdatedCategory);
		
		Category updatedCategory = categoryService.update(expectedUpdatedCategory, VALID_CATEGORY_ID);
		
		assertThat(updatedCategory, is(equalTo(expectedUpdatedCategory)));
	}
	
	@Test
	void shouldThrowValidationExceptionWhenUpdateCategoryWithAInvalidId() {
		Category expectedUpdatedCategory = new Category(1, "Books");
		
		when(categoryRepository.existsById(VALID_CATEGORY_ID)).thenReturn(false);
		
		assertThrows(ValidationException.class, () -> categoryService.update(expectedUpdatedCategory, VALID_CATEGORY_ID));
	}
}
