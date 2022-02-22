package br.com.cursoudemy.productapi.domain.modules.sales.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SalesClientIT {
	
	@Value("${test.token}")
	private String TEST_TOKEN;
	
	@LocalServerPort
	private int port;
	
	@BeforeEach
	void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/api/products/{id}/sales";
	}
	
	@Test
	void shouldReturnOkAndProductWithSalesWhenGetByIdCalled() {
		RestAssured.given()
				.auth()
					.oauth2(TEST_TOKEN)
				.header("transactionid", UUID.randomUUID())
				.accept(ContentType.JSON)
				.pathParam("id", 1001)
			.when()
				.get()
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("name", equalTo("Crise nas Infinitas Terras"))
				.body("sales", hasSize(2));
	}
	
	@Test
	void shouldReturnOkAndProductWithNoSalesWhenGetByIdCalledAndProductHasNoSales() {
		RestAssured.given()
				.auth()
					.oauth2(TEST_TOKEN)
				.header("transactionid", UUID.randomUUID())
				.accept(ContentType.JSON)
				.pathParam("id", 1004)
			.when()
				.get()
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("name", equalTo("Harry Potter e o Cálice de Fogo"))
				.body("sales", hasSize(0));
	}
}
