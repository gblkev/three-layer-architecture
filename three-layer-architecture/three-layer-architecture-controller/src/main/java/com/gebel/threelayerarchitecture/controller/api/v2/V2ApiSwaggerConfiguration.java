package com.gebel.threelayerarchitecture.controller.api.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.V1ApiBaseUri;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorCodeDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.V2ApiBaseUri;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;

@Configuration
class V2ApiSwaggerConfiguration {

	@Bean
	GroupedOpenApi v2Apis() {
		return GroupedOpenApi.builder()
			.group("v2")
			.pathsToMatch(V2ApiBaseUri.API_V2_BASE_URI + "/**")
			.pathsToExclude(V1ApiBaseUri.API_V1_BASE_URI + "/**")
			.addOpenApiCustomiser(buildV2OpenAPI())
			.addOperationCustomizer(new ReplaceApiBusinessErrorCodeEnumValuesInApiResponses())
			.build();
	}
	
	private OpenApiCustomiser buildV2OpenAPI() {
		Schema<?> apiBusinessErrorDtoSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(ApiBusinessErrorDto.class)).schema;
		return openApi -> openApi
			.info(openApi.getInfo().version("v2"))
			.getComponents().addSchemas(ApiBusinessErrorDto.class.getSimpleName(), apiBusinessErrorDtoSchema);
	}
	
	private class ReplaceApiBusinessErrorCodeEnumValuesInApiResponses implements OperationCustomizer {
		
		private final String API_BUSINESS_ERROR_CODE_PREFIX = ApiBusinessErrorCodeDto.class.getSimpleName() + ".";
		
		@Override
		public Operation customize(Operation operation, HandlerMethod handlerMethod) {
			List<String> responseCodesOfApiResponsesToBeRemoved = new ArrayList<>();
			ApiResponse apiResponsesToBeAdded = null;
			String targetHttpResponseCodeAsString = String.valueOf(ApiBusinessErrorDto.CONFLICT_HTTP_CODE.value());
			for (Map.Entry<String, ApiResponse> entry : operation.getResponses().entrySet()) {
				String responseCode = entry.getKey();
				
				if (!isBusinessErrorApiResponse(responseCode)) {
					continue;
				}
				
				ApiBusinessErrorCodeDto apiBusinessErrorCodeDto = parseApiBusinessErrorCode(responseCode);
				boolean isAnotherBusinessErrorAlreadyPresent = (apiResponsesToBeAdded != null);
				if (isAnotherBusinessErrorAlreadyPresent) {
					concatenateBusinessErrorToApiResponse(apiResponsesToBeAdded, apiBusinessErrorCodeDto);
				}
				else {
					apiResponsesToBeAdded = buildBusinessErrorApiResponse(apiBusinessErrorCodeDto);
				}
				
				responseCodesOfApiResponsesToBeRemoved.add(responseCode);
			}
			
			responseCodesOfApiResponsesToBeRemoved.forEach(operation.getResponses()::remove);
			if (apiResponsesToBeAdded != null) {
				operation.getResponses().addApiResponse(targetHttpResponseCodeAsString, apiResponsesToBeAdded);
			}
			
			return operation;
		}
		
		private boolean isBusinessErrorApiResponse(String responseCode) {
			return (responseCode != null && responseCode.startsWith(API_BUSINESS_ERROR_CODE_PREFIX));
		}
		
		private ApiBusinessErrorCodeDto parseApiBusinessErrorCode(String responseCode) {
			String enumValueAsString = responseCode.substring(API_BUSINESS_ERROR_CODE_PREFIX.length());
			return ApiBusinessErrorCodeDto.valueOf(enumValueAsString);
		}
		
		private ApiResponse buildBusinessErrorApiResponse(ApiBusinessErrorCodeDto businessErrorCode) {
			String description = buildBusinessErrorDescription(businessErrorCode);
			
			Schema<?> apiBusinessErrorDtoSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(ApiBusinessErrorDto.class)).schema;
			apiBusinessErrorDtoSchema.setTitle(ApiBusinessErrorDto.class.getSimpleName());
			
			MediaType mediaType = new MediaType().schema(apiBusinessErrorDtoSchema);
			Content content = new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType);
			
			ApiResponse apiResponse = new ApiResponse();
			apiResponse.setDescription(description);
			apiResponse.setContent(content);
			return apiResponse;
		}
		
		private String buildBusinessErrorDescription(ApiBusinessErrorCodeDto businessErrorCode) {
			return "errorCode " + ApiBusinessErrorCodeDto.class.getSimpleName() + "." + businessErrorCode.name() + ": " + businessErrorCode.getDescription();
		}
		
		private void concatenateBusinessErrorToApiResponse(ApiResponse apiResponse, ApiBusinessErrorCodeDto businessErrorCode) {
			String newDescription = buildBusinessErrorDescription(businessErrorCode) + "<br><br>" + apiResponse.getDescription();
			apiResponse.setDescription(newDescription);
		}
		
	}

}