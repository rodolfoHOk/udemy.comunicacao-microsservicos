package br.com.cursoudemy.productapi.core.openapi;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.cursoudemy.productapi.api.exceptionhandler.ProblemDetails;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

@Configuration
public class SpringFoxConfig {

	@Bean
	public Docket apiDocketV1() {
		var typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.OAS_30)
				.groupName("Version 1")
				.select()
					.apis(RequestHandlerSelectors.basePackage("br.com.cursoudemy.productapi.api"))
					.paths(PathSelectors.any())
					.build()
				.apiInfo(apiInfoV1())
				.additionalModels(typeResolver.resolve(ProblemDetails.class))
				.globalResponses(HttpMethod.GET, globalGetResponses())
				.globalResponses(HttpMethod.POST, globalPostPutResponses())
				.globalResponses(HttpMethod.PUT, globalPostPutResponses())
				.globalResponses(HttpMethod.DELETE, globalDeleteResponses())
				.ignoredParameterTypes(WebRequest.class)
				.globalRequestParameters(requestParameters())
				.securitySchemes(Collections.singletonList(securityScheme()))
				.securityContexts(Collections.singletonList(securityContext()));
	}
	
	private List<RequestParameter> requestParameters() {
		RequestParameter transactionidParameter = new RequestParameterBuilder()
				.name("transactionid")
				.in(ParameterType.HEADER)
				.required(true)
				.description("Transaction ID")
				.build();
		return Collections.singletonList(transactionidParameter);
	}
	
	private ApiInfo apiInfoV1() {
		return new ApiInfoBuilder()
				.title("Product API")
				.description("Product API Microsservice of Udemy course: Comunicação entre microsserviços")
				.version("1")
				.contact(new Contact("HiOkTec", "https://github.com/rodolfoHOk", "hioktec@gmail.com"))
				.build();
	}
	
	private SecurityScheme securityScheme() {
		return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("Authorization").build();
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(Collections.singletonList(securityReference()))
				.operationSelector(operationContext -> true)
				.build();
	}
	
	private SecurityReference securityReference() {
		return SecurityReference.builder()
				.reference("Authorization")
				.scopes(scopes())
				.build();
	}
	
	private AuthorizationScope[] scopes() {
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[2];
		authorizationScopes[0] = new AuthorizationScope("READ", "read access");
		authorizationScopes[1] = new AuthorizationScope("WRITE", "write access");
		return authorizationScopes;
	}
	
	private List<Response> globalGetResponses() {
		return Arrays.asList(
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Internal server error")
						.representation(MediaType.APPLICATION_JSON)
						.apply(getProblemDetailModelReference())
						.build(),
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
						.description("Resource cannot produce a consumer-accepted representation")
						.build()
				);
	}
	
	private List<Response> globalPostPutResponses() {
		return Arrays.asList(
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
						.description("Invalid request (client error)")
						.representation(MediaType.APPLICATION_JSON)
						.apply(getProblemDetailModelReference())
						.build(),
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Internal server error")
						.representation(MediaType.APPLICATION_JSON)
						.apply(getProblemDetailModelReference())
						.build(),
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
						.description("Resource cannot produce a consumer-accepted representation")
						.build(),
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
						.description("Request body is in an unsupported format")
						.representation(MediaType.APPLICATION_JSON)
						.apply(getProblemDetailModelReference())
						.build(),
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
						.description("Unauthorized access")
						.build()
				);
	}
	
	private List<Response> globalDeleteResponses() {
		return Arrays.asList(
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
						.description("Invalid request (client error)")
						.representation(MediaType.APPLICATION_JSON)
						.apply(getProblemDetailModelReference())
						.build(),
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Internal server error")
						.representation(MediaType.APPLICATION_JSON)
						.apply(getProblemDetailModelReference())
						.build(),
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
						.description("Unauthorized access")
						.build()
				);
	}
	
	private Consumer<RepresentationBuilder> getProblemDetailModelReference() {
		return rep -> rep.model(model -> model.name("ProblemDetails")
				.referenceModel(ref -> ref.key(key -> key.qualifiedModelName(
						qMName -> qMName.name("ProblemDetails").namespace(
								"br.com.cursoudemy.productapi.api.exceptionhandler")))));
	}
	
	@Bean // Para resolver o problema de serialização de OffsetDateTime
	public JacksonModuleRegistrar springfoxJacksonConfig() {
		return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
	}	
	
	@Bean // bean necessário springfox 3.0.0 issues in springboot 2.6.0+
	public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
	    return new BeanPostProcessor() {

	        @Override
	        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
	            if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
	                customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
	            }
	            return bean;
	        }

	        private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
	            List<T> copy = mappings.stream()
	                    .filter(mapping -> mapping.getPatternParser() == null)
	                    .collect(Collectors.toList());
	            mappings.clear();
	            mappings.addAll(copy);
	        }

	        @SuppressWarnings("unchecked")
	        private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
	            try {
	                Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
	                field.setAccessible(true);
	                return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
	            } catch (IllegalArgumentException | IllegalAccessException e) {
	                throw new IllegalStateException(e);
	            }
	        }
	    };
	}
	
}
