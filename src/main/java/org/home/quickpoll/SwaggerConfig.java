package org.home.quickpoll;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.models.dto.builder.ApiInfoBuilder;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger
public class SwaggerConfig {

    @Autowired
    private SpringSwaggerConfig springSwaggerConfig;

    public ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("QuickPoll REST API")
                .description("QuickPoll Api for creating and managing                                      polls")
                .termsOfServiceUrl("http://example.com/terms-of-service")
                .contact("info@example.com")
                .license("MIT License")
                .licenseUrl("http://opensource.org/licenses/MIT")
                .build();
    }

    @Bean
    public SwaggerSpringMvcPlugin v1APIConfiguration() {
        return  new SwaggerSpringMvcPlugin(springSwaggerConfig).apiInfo(getApiInfo())
                .apiVersion("1.0")
                .includePatterns("/v1/*.*").swaggerGroup("v1")
                .useDefaultResponseMessages(false);
    }

    @Bean
    public SwaggerSpringMvcPlugin v2ApiConfiguration() {
        return  new SwaggerSpringMvcPlugin(springSwaggerConfig)
                .apiInfo(getApiInfo())
                .apiVersion("2.0")
                .includePatterns("/v2/*.*").swaggerGroup("v2")
                .useDefaultResponseMessages(false);
    }
}
