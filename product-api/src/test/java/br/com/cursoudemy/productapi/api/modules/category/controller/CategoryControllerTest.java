package br.com.cursoudemy.productapi.api.modules.category.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import br.com.cursoudemy.productapi.api.dto.SuccessResponse;
import br.com.cursoudemy.productapi.api.exceptionhandler.ApiExceptionHandler;
import br.com.cursoudemy.productapi.api.modules.category.assembler.CategoryRequestDisassembler;
import br.com.cursoudemy.productapi.api.modules.category.assembler.CategoryResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.category.dto.CategoryRequest;
import br.com.cursoudemy.productapi.domain.exception.EntityInUseException;
import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.modules.category.model.Category;
import br.com.cursoudemy.productapi.domain.modules.category.service.CategoryService;
import br.com.cursoudemy.productapi.utils.JsonConversionUnit;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

	@Mock
	private CategoryService categoryService;
	
	@InjectMocks
	private CategoryController categoryController;
	
	private MockMvc mockMvc;
	
	private static final String BASE_URL = "/api/categories";
	private static final Integer VALID_ID = 1;
	private static final Integer INVALID_ID = 100;
	private static final String VALID_DESCRIPTION = "hardware";
	private static final String INVALID_DESCRIPTION = "fruits";
	
	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
				.setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
				.setControllerAdvice(new ApiExceptionHandler())
				.build();
	}
	
	@Test
	void shouldReturnCreatedAndCreatedCategoryWhenPostIsCalled() throws Exception {
		// given
		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setDescription(VALID_DESCRIPTION);
		Category savedCategory = new Category(VALID_ID, VALID_DESCRIPTION);
		String expectedJson = JsonConversionUnit.asJsonString(
				CategoryResponseAssembler.toModel(savedCategory));
		// when 
		when(categoryService.save(CategoryRequestDisassembler.toDomainObject(categoryRequest)))
			.thenReturn(savedCategory);
		// then
		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(categoryRequest)))
			.andExpect(status().isCreated())
			.andExpect(content().string(expectedJson));
	}
	
	@Test
	void shouldReturnBadRequestWhenPostIsCalledWithoutRequiredField() throws Exception {
		CategoryRequest categoryRequest = new CategoryRequest();
		
		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(categoryRequest)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnOkAndListOfCategoryWhenGetIsCalled() throws Exception {
		Category foundCategory = new Category(VALID_ID, VALID_DESCRIPTION);
		
		when(categoryService.findAll()).thenReturn(Collections.singletonList(foundCategory));
		
		mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(equalTo(foundCategory.getId()))))
			.andExpect(jsonPath("$[0].description", is(equalTo(foundCategory.getDescription()))));
	}
	
	@Test
	void shouldReturnOkAndEmptyListWhenGetIsCalled() throws Exception {
		when(categoryService.findAll()).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	void shouldReturnOkAndACategoryWhenGetByIdIsCalled() throws Exception {
		Category foundCategory = new Category(VALID_ID, VALID_DESCRIPTION);
		
		when(categoryService.findById(VALID_ID)).thenReturn(foundCategory);
		
		mockMvc.perform(get(BASE_URL + "/" + VALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(foundCategory.getId()))))
			.andExpect(jsonPath("$.description", is(equalTo(foundCategory.getDescription()))));
	}
	
	@Test
	void shouldReturnNotFoundWhenGetByIdIsCalledWithInvalidId() throws Exception {
		when(categoryService.findById(INVALID_ID)).thenThrow(ResourceNotFoundException.class);
		
		mockMvc.perform(get(BASE_URL + "/" + INVALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnOkAndListOfCategoryWhenGetByDescriptionIsCalled() throws Exception {
		Category foundCategory = new Category(VALID_ID, VALID_DESCRIPTION);
		
		when(categoryService.findByDescription(VALID_DESCRIPTION))
			.thenReturn(Collections.singletonList(foundCategory));
		
		mockMvc.perform(get(BASE_URL + "/description/" + VALID_DESCRIPTION).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(equalTo(foundCategory.getId()))))
			.andExpect(jsonPath("$[0].description", is(equalTo(foundCategory.getDescription()))));
	}
	
	@Test
	void shouldReturnOkAndAEmptyListWhenGetByDescriptionIsCalledWithInvalidDescription() throws Exception {
		when(categoryService.findByDescription(INVALID_DESCRIPTION)).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(BASE_URL + "/description/" + INVALID_DESCRIPTION).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	void shouldReturnOkAndASuccessResponseWhenDeleteByIdIsCalled() throws Exception {
		SuccessResponse expectedSuccessResponse = SuccessResponse.create("The category was deleted.");
		String expectedJson = JsonConversionUnit.asJsonString(expectedSuccessResponse);
		
		doNothing().when(categoryService).delete(VALID_ID);
		
		mockMvc.perform(delete(BASE_URL + "/" + VALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(expectedJson));
	}
	
	@Test
	void shouldReturnNotFoundWhenDeleteByIdIsCalledWithInvalidId() throws Exception {
		doThrow(ResourceNotFoundException.class).when(categoryService).delete(INVALID_ID);
		
		mockMvc.perform(delete(BASE_URL + "/" + INVALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnBadRequestWhenDeleteByIdIsCalledButCategoryInUse() throws Exception {
		doThrow(EntityInUseException.class).when(categoryService).delete(INVALID_ID);
		
		mockMvc.perform(delete(BASE_URL + "/" + INVALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnOkAndUpdatedCategoryWhenUpdateByIdIsCalled() throws Exception {
		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setDescription(VALID_DESCRIPTION);
		Category updatedCategory = new Category(VALID_ID, VALID_DESCRIPTION);

		when(categoryService.update(CategoryRequestDisassembler.toDomainObject(categoryRequest), VALID_ID))
			.thenReturn(updatedCategory);
		
		mockMvc.perform(put(BASE_URL + "/" + VALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(categoryRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(VALID_ID))))
			.andExpect(jsonPath("$.description", is(equalTo(categoryRequest.getDescription()))));
	}
	
	@Test
	void shouldReturnNotFoundWhenUpdateByIdIsCalledWithInvalidId() throws Exception {
		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setDescription(VALID_DESCRIPTION);

		doThrow(ResourceNotFoundException.class).when(categoryService)
			.update(CategoryRequestDisassembler.toDomainObject(categoryRequest), INVALID_ID);
		
		mockMvc.perform(put(BASE_URL + "/" + INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(categoryRequest)))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnBadRequestWhenUpdateByIdIsCalledWithoutRequiredField() throws Exception {
		CategoryRequest categoryRequest = new CategoryRequest();
		
		mockMvc.perform(put(BASE_URL + "/" + INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(categoryRequest)))
			.andExpect(status().isBadRequest());
	}
	
}
