package br.com.cursoudemy.productapi.api.modules.product.openapi;

import java.util.List;

import org.springframework.web.context.request.WebRequest;

import br.com.cursoudemy.productapi.api.dto.SuccessResponse;
import br.com.cursoudemy.productapi.api.exceptionhandler.ProblemDetails;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductCheckStockRequest;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductRequest;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductResponse;
import br.com.cursoudemy.productapi.api.modules.product.dto.ProductSalesResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api(tags = "Products")
public interface ProductControllerOpenApi {

	@ApiOperation("Create a product")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Product created")
	})
	ProductResponse save(
			@ApiParam(name = "body", value = "Representation of product registration request", required = true) 
			ProductRequest productRequest);

	@ApiOperation("List all products")
	@ApiResponses({
		@ApiResponse(responseCode = "401", description = "Unauthorized access")
	})
	List<ProductResponse> findAll();

	@ApiOperation("Search a product by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Product ID invalid",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
		@ApiResponse(responseCode = "404", description = "Product not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	ProductResponse findById(
			@ApiParam(value = "Product ID", example = "1", required = true) 
			Integer id);

	@ApiOperation("Search a product by name")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Product name invalid",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized access")
	})
	List<ProductResponse> findByName(
			@ApiParam(value = "Product name", example = "Books", required = true) 
			String name);

	@ApiOperation("Search a product by category ID")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Category ID invalid",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized access")
	})
	List<ProductResponse> findByCategoryId(
			@ApiParam(value = "Category ID", example = "1", required = true)
			Integer categoryId);

	@ApiOperation("Search a product by supplier ID")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Supplier ID invalid",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized access")
	})
	List<ProductResponse> findBySupplierId(
			@ApiParam(value = "Supplier ID", example = "1", required = true)
			Integer supplierId);

	@ApiOperation("Delete a product by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Product deleted"),
		@ApiResponse(responseCode = "404", description = "Product not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	SuccessResponse delete(
			@ApiParam(value = "Product ID", example = "1", required = true) 
			Integer id);

	@ApiOperation("Update a product by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Product updated"),
		@ApiResponse(responseCode = "404", description = "Product not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	ProductResponse update(
			@ApiParam(value = "Product ID", example = "1", required = true) 
			Integer id, 
			@ApiParam(name = "body", value = "Representation of product registration request", required = true) 
			ProductRequest productRequest);

	@ApiOperation("Search a product by ID with sales IDs list")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Product ID invalid",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
		@ApiResponse(responseCode = "404", description = "Product not found",
				content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
	})
	ProductSalesResponse findProductSales(
			@ApiParam(value = "Product ID", example = "1", required = true) 
			Integer id);

	@ApiOperation("Check if the products are in stock")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Products stock is ok")
	})
	SuccessResponse checkProductsStock(
			@ApiParam(name = "body", value = "Representation of in-stock product list verification request", required = true) 
			ProductCheckStockRequest productCheckStockRequest, 
			@ApiParam(hidden = true)
			WebRequest request);

}