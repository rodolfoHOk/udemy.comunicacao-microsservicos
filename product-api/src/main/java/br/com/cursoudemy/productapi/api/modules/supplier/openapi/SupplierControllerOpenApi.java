package br.com.cursoudemy.productapi.api.modules.supplier.openapi;

import java.util.List;

import br.com.cursoudemy.productapi.api.dto.SuccessResponse;
import br.com.cursoudemy.productapi.api.exceptionhandler.ProblemDetails;
import br.com.cursoudemy.productapi.api.modules.supplier.dto.SupplierRequest;
import br.com.cursoudemy.productapi.api.modules.supplier.dto.SupplierResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api(tags = "Suppliers")
public interface SupplierControllerOpenApi {

	@ApiOperation("Create a supplier")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Supplier created")
	})
	SupplierResponse save(
			@ApiParam(name = "body", value = "Representation of supplier registration request", required = true) 
			SupplierRequest supplierRequest);

	@ApiOperation("List all suppliers")
	List<SupplierResponse> findAll();

	@ApiOperation("Search a supplier by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Supplier ID invalid",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
		@ApiResponse(responseCode = "404", description = "Supplier not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	SupplierResponse findById(
			@ApiParam(value = "Supplier ID", example = "1", required = true) 
			Integer id);

	@ApiOperation("Search a supplier by name")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Supplier name invalid",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	List<SupplierResponse> findByName(
			@ApiParam(value = "Supplier name", example = "Saraiva bookstore", required = true) 
			String name);

	@ApiOperation("Delete a supplier by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Supplier deleted"),
		@ApiResponse(responseCode = "404", description = "Supplier not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	SuccessResponse delete(
			@ApiParam(value = "Supplier ID", example = "1", required = true) 
			Integer id);

	@ApiOperation("Update a supplier by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Supplier updated"),
		@ApiResponse(responseCode = "404", description = "Supplier not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	SupplierResponse update(
			@ApiParam(value = "Supplier ID", example = "1", required = true)
			Integer id, 
			@ApiParam(name = "body", value = "Representation of supplier registration request", required = true) 
			SupplierRequest supplierRequest);

}