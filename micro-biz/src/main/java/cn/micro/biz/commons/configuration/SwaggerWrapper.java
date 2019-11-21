package cn.micro.biz.commons.configuration;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.auth.NonAuth;
import cn.micro.biz.commons.auth.PreAuth;
import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.DocumentationBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Swagger Wrapper
 *
 * @author lry
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "micro", name = "swagger", havingValue = "true")
public class SwaggerWrapper implements ApplicationListener<ApplicationStartedEvent> {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public Docket api() {
        // Api Info
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        if (!(applicationName == null || applicationName.length() == 0)) {
            apiInfoBuilder.title(applicationName);
        }
        ApiInfo apiInfo = apiInfoBuilder.build();
        // security schemes
        List<ApiKey> securitySchemes = Collections.singletonList(
                new ApiKey(HttpHeaders.AUTHORIZATION, MicroAuthContext.ACCESS_TOKEN_HEADER_KEY, "header"));
        // security contexts
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        List<SecurityReference> defaultAuth = Collections.singletonList(
                new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes));
        List<SecurityContext> securityContexts = Collections.singletonList(
                SecurityContext.builder().securityReferences(defaultAuth).forPaths(
                        PathSelectors.regex("^(?!auth).*$")).build());

        // docket
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .securitySchemes(securitySchemes)
                .securityContexts(securityContexts);
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, MicroSpringUtils.MicroRequestMappingInfo> requestMappingInfoMap = MicroSpringUtils.REQUEST_MAPPING_INFO_MAP;

        DocumentationCache documentationCache = MicroSpringUtils.APPLICATION_CONTEXT.getBean(DocumentationCache.class);
        Map<String, Documentation> documentationMap = documentationCache.all();
        for (Map.Entry<String, Documentation> entry : documentationMap.entrySet()) {
            Documentation documentation = entry.getValue();

            // 设置API描述
            Map<String, String> apiAuthMap = new HashMap<>();
            Multimap<String, ApiListing> newApiListings = TreeMultimap.create(
                    Ordering.natural(), DocumentationBuilder.byListingPosition());
            for (Map.Entry<String, ApiListing> apiEntry : documentation.getApiListings().entries()) {
                ApiListing apiListing = apiEntry.getValue();

                List<ApiDescription> apiDescriptionList = new ArrayList<>();
                for (ApiDescription apiDescription : apiListing.getApis()) {
                    List<Operation> newOperations = new ArrayList<>();
                    for (Operation operation : apiDescription.getOperations()) {
                        List<SecurityReference> securityReferenceList = new ArrayList<>();
                        for (Map.Entry<String, List<AuthorizationScope>> map : operation.getSecurityReferences().entrySet()) {
                            securityReferenceList.add(new SecurityReference(
                                    map.getKey(), map.getValue().toArray(new AuthorizationScope[0])));
                        }

                        String authIcon = "";
                        String requestMappingUniqueId = operation.getMethod().name() + "@" + apiDescription.getPath();
                        MicroSpringUtils.MicroRequestMappingInfo requestMappingInfo = requestMappingInfoMap.get(requestMappingUniqueId);
                        if (requestMappingInfo != null) {
                            Method method = requestMappingInfo.getHandlerMethod().getMethod();
                            if (method.isAnnotationPresent(PreAuth.class) || !method.isAnnotationPresent(NonAuth.class)) {
                                authIcon = "\uD83D\uDD11";
                                if (!apiAuthMap.containsKey(apiEntry.getKey())) {
                                    apiAuthMap.put(apiEntry.getKey(), authIcon);
                                }
                            }
                        }

                        Operation newOperation = newOperation(operation, authIcon, securityReferenceList);
                        newOperations.add(newOperation);
                    }

                    ApiDescription newApiDescription = newApiDescription(apiDescription, newOperations);
                    apiDescriptionList.add(newApiDescription);
                }

                ApiListing newApiListing = newApiListing(apiListing, apiDescriptionList);
                newApiListings.put(apiEntry.getKey(), newApiListing);
            }

            // 设置类描述
            Set<Tag> newTagSet = new LinkedHashSet<>();
            for (Tag tag : documentation.getTags()) {
                // 自定义描述内容
                String description = tag.getDescription();
                String authIcon = apiAuthMap.get(tag.getName());
                if (authIcon != null) {
                    description = authIcon + tag.getDescription();
                }
                newTagSet.add(new Tag(tag.getName(), description, tag.getOrder(), tag.getVendorExtensions()));
            }

            documentationCache.addDocumentation(newDocumentation(documentation, newTagSet, newApiListings));
        }
    }

    private ApiDescription newApiDescription(ApiDescription apiDescription, List<Operation> newOperations) {
        return new ApiDescription(
                null,
                apiDescription.getPath(),
                apiDescription.getDescription(),
                newOperations,
                apiDescription.isHidden());
    }

    private Operation newOperation(Operation operation, String authIcon, List<SecurityReference> securityReferenceList) {
        return new Operation(operation.getMethod(),
                authIcon + operation.getSummary(),
                // 自定义Note
                operation.getNotes(),
                operation.getResponseModel(),
                // 自定义NickName
                operation.getUniqueId(),
                operation.getPosition(),
                operation.getTags(),
                operation.getProduces(),
                operation.getConsumes(),
                operation.getProtocol(),
                securityReferenceList,
                operation.getParameters(),
                operation.getResponseMessages(),
                operation.getDeprecated(),
                operation.isHidden(),
                operation.getVendorExtensions());
    }

    private ApiListing newApiListing(ApiListing apiListing, List<ApiDescription> apiDescriptionList) {
        return new ApiListing(apiListing.getApiVersion(),
                apiListing.getBasePath(),
                apiListing.getResourcePath(),
                apiListing.getProduces(),
                apiListing.getConsumes(),
                apiListing.getHost(),
                apiListing.getProtocols(),
                apiListing.getSecurityReferences(),
                apiDescriptionList,
                apiListing.getModels(),
                apiListing.getDescription(),
                apiListing.getPosition(),
                apiListing.getTags());
    }

    private Documentation newDocumentation(Documentation documentation, Set<Tag> newTagSet, Multimap<String, ApiListing> apiListings) {
        return new Documentation(
                documentation.getGroupName(),
                documentation.getBasePath(),
                newTagSet,
                apiListings,
                documentation.getResourceListing(),
                new LinkedHashSet<>(documentation.getProduces()),
                new LinkedHashSet<>(documentation.getConsumes()),
                documentation.getHost(),
                new LinkedHashSet<>(documentation.getSchemes()),
                documentation.getVendorExtensions());
    }

}
