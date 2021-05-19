/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config;

import com.leimingtech.commons.tools.constant.Constant;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Swagger配置
 *
 * @since 1.0.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //@Value("${project.host}")
    //private String host;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                //.host(host)
                .apiInfo(apiInfo())
                .select()
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //包下的类，才生成接口文档
                //.apis(RequestHandlerSelectors.basePackage("com.leimingtech.controller"))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(java.util.Date.class, String.class)
                .securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("雷铭电商7.0")
                .description("商家端开发文档")
                .termsOfServiceUrl("https://www.leimingtech.com/community")
                .version("1.1.0")
                .build();
    }

    private List<ApiKey> security() {
        return newArrayList(
                new ApiKey(Constant.SELLER_TOKEN_HEADER, Constant.SELLER_TOKEN_HEADER, "header")
        );
    }

}