package br.com.cursoudemy.productapi.api.modules.supplier.controller;

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
import br.com.cursoudemy.productapi.api.modules.supplier.assembler.SupplierRequestDisassembler;
import br.com.cursoudemy.productapi.api.modules.supplier.assembler.SupplierResponseAssembler;
import br.com.cursoudemy.productapi.api.modules.supplier.dto.SupplierRequest;
import br.com.cursoudemy.productapi.domain.exception.EntityInUseException;
import br.com.cursoudemy.productapi.domain.exception.ResourceNotFoundException;
import br.com.cursoudemy.productapi.domain.modules.supplier.model.Supplier;
import br.com.cursoudemy.productapi.domain.modules.supplier.service.SupplierService;
import br.com.cursoudemy.productapi.utils.JsonConversionUnit;

@ExtendWith(MockitoExtension.class)
public class SupplierControllerTest {
	
	@Mock
	private SupplierService supplierService;
	
	@InjectMocks
	private SupplierController supplierController;
	
	private MockMvc mockMvc;
	
	private static final String BASE_URL = "/api/suppliers";
	private static final Integer VALID_ID = 1;
	private static final Integer INVALID_ID = 100;
	private static final String VALID_NAME = "Amazon WebStore";
	private static final String INVALID_NAME = "Saraiva BookStore";
	
	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(supplierController)
				.setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
				.setControllerAdvice(new ApiExceptionHandler())
				.build();
	}
	
	@Test
	void shouldReturnCreatedAndCreatedSupplierWhenPostIsCalled() throws Exception {
		SupplierRequest supplierRequest = new SupplierRequest();
		supplierRequest.setName(VALID_NAME);
		Supplier savedSupplier = new Supplier(VALID_ID, VALID_NAME);
		String expectedJson = JsonConversionUnit.asJsonString(
				SupplierResponseAssembler.toModel(savedSupplier));

		when(supplierService.save(SupplierRequestDisassembler.toDomainObject(supplierRequest)))
			.thenReturn(savedSupplier);

		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(supplierRequest)))
			.andExpect(status().isCreated())
			.andExpect(content().string(expectedJson));
	}
	
	@Test
	void shouldReturnBadRequestWhenPostIsCalledWithoutRequiredField() throws Exception {
		SupplierRequest supplierRequest = new SupplierRequest();
		
		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(supplierRequest)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnOkAndListOfSupplierWhenGetIsCalled() throws Exception {
		Supplier foundSupplier = new Supplier(VALID_ID, VALID_NAME);
		
		when(supplierService.findAll()).thenReturn(Collections.singletonList(foundSupplier));
		
		mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(equalTo(foundSupplier.getId()))))
			.andExpect(jsonPath("$[0].name", is(equalTo(foundSupplier.getName()))));
	}
	
	@Test
	void shouldReturnOkAndEmptyListWhenGetIsCalled() throws Exception {
		when(supplierService.findAll()).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	void shouldReturnOkAndASupplierWhenGetByIdIsCalled() throws Exception {
		Supplier foundSupplier = new Supplier(VALID_ID, VALID_NAME);
		
		when(supplierService.findById(VALID_ID)).thenReturn(foundSupplier);
		
		mockMvc.perform(get(BASE_URL + "/" + VALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(foundSupplier.getId()))))
			.andExpect(jsonPath("$.name", is(equalTo(foundSupplier.getName()))));
	}
	
	@Test
	void shouldReturnNotFoundWhenGetByIdIsCalledWithInvalidId() throws Exception {
		when(supplierService.findById(INVALID_ID)).thenThrow(ResourceNotFoundException.class);
		
		mockMvc.perform(get(BASE_URL + "/" + INVALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnOkAndListOfSupplierWhenGetByNameIsCalled() throws Exception {
		Supplier foundSupplier = new Supplier(VALID_ID, VALID_NAME);
		
		when(supplierService.findByName(VALID_NAME))
			.thenReturn(Collections.singletonList(foundSupplier));
		
		mockMvc.perform(get(BASE_URL + "/name/" + VALID_NAME).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(equalTo(foundSupplier.getId()))))
			.andExpect(jsonPath("$[0].name", is(equalTo(foundSupplier.getName()))));
	}
	
	@Test
	void shouldReturnOkAndAEmptyListWhenGetByNameIsCalledWithInvalidName() throws Exception {
		when(supplierService.findByName(INVALID_NAME)).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(BASE_URL + "/name/" + INVALID_NAME).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	void shouldReturnOkAndASuccessResponseWhenDeleteByIdIsCalled() throws Exception {
		SuccessResponse expectedSuccessResponse = SuccessResponse.create("The supplier was deleted.");
		String expectedJson = JsonConversionUnit.asJsonString(expectedSuccessResponse);
		
		doNothing().when(supplierService).delete(VALID_ID);
		
		mockMvc.perform(delete(BASE_URL + "/" + VALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(expectedJson));
	}
	
	@Test
	void shouldReturnNotFoundWhenDeleteByIdIsCalledWithInvalidId() throws Exception {
		doThrow(ResourceNotFoundException.class).when(supplierService).delete(INVALID_ID);
		
		mockMvc.perform(delete(BASE_URL + "/" + INVALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnBadRequestWhenDeleteByIdIsCalledButSupplierInUse() throws Exception {
		doThrow(EntityInUseException.class).when(supplierService).delete(INVALID_ID);
		
		mockMvc.perform(delete(BASE_URL + "/" + INVALID_ID).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnOkAndUpdatedSupplierWhenUpdateByIdIsCalled() throws Exception {
		SupplierRequest supplierRequest = new SupplierRequest();
		supplierRequest.setName(VALID_NAME);
		Supplier updatedSupplier = new Supplier(VALID_ID, VALID_NAME);

		when(supplierService.update(SupplierRequestDisassembler.toDomainObject(supplierRequest), VALID_ID))
			.thenReturn(updatedSupplier);
		
		mockMvc.perform(put(BASE_URL + "/" + VALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(supplierRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(VALID_ID))))
			.andExpect(jsonPath("$.name", is(equalTo(supplierRequest.getName()))));
	}
	
	@Test
	void shouldReturnNotFoundWhenUpdateByIdIsCalledWithInvalidId() throws Exception {
		SupplierRequest supplierRequest = new SupplierRequest();
		supplierRequest.setName(VALID_NAME);

		doThrow(ResourceNotFoundException.class).when(supplierService)
			.update(SupplierRequestDisassembler.toDomainObject(supplierRequest), INVALID_ID);
		
		mockMvc.perform(put(BASE_URL + "/" + INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(supplierRequest)))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnBadRequestWhenUpdateByIdIsCalledWithoutRequiredField() throws Exception {
		SupplierRequest supplierRequest = new SupplierRequest();
		
		mockMvc.perform(put(BASE_URL + "/" + INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConversionUnit.asJsonString(supplierRequest)))
			.andExpect(status().isBadRequest());
	}
	
}
