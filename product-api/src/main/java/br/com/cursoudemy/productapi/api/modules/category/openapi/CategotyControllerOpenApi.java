package br.com.cursoudemy.productapi.api.modules.category.openapi;

import java.util.List;

import br.com.cursoudemy.productapi.api.dto.SuccessResponse;
import br.com.cursoudemy.productapi.api.exceptionhandler.ProblemDetails;
import br.com.cursoudemy.productapi.api.modules.category.dto.CategoryRequest;
import br.com.cursoudemy.productapi.api.modules.category.dto.CategoryResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Api(tags = "Categories")
public interface CategotyControllerOpenApi {

	@ApiOperation("Create a category")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Category created")
	})
	CategoryResponse save(
			@ApiParam(name = "body", value = "Representation of category registration request", required = true) 
			CategoryRequest categoryRequest);

	@ApiOperation("List all categories")
	List<CategoryResponse> findAll();

	@ApiOperation("Search a category by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Category ID invalid",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
		@ApiResponse(responseCode = "404", description = "Category not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	CategoryResponse findById(
			@ApiParam(value = "Category ID", example = "1", required = true) 
			Integer id);

	@ApiOperation("Search a category by description")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Category description invalid",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	List<CategoryResponse> findByDescription(
			@ApiParam(value = "Category description", example = "Books", required = true) 
			String description);

	@ApiOperation("Delete a category by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Category deleted"),
		@ApiResponse(responseCode = "404", description = "Category not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	SuccessResponse delete(
			@ApiParam(value = "Category ID", example = "1", required = true) 
			Integer id);
	
	@ApiOperation("Update a category by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Category updated"),
		@ApiResponse(responseCode = "404", description = "Category not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	CategoryResponse update(
			@ApiParam(value = "Category ID", example = "1", required = true) 
			Integer id, 
			@ApiParam(name = "body", value = "Representation of category registration request", required = true) 
			CategoryRequest categoryRequest);

}