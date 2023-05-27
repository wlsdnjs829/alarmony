package com.slembers.alarmony.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /*
        SWAGGER를 활용 중이시라면, DTO에

        @Schema(description = "")
        @Tag(name = "")
        @Operation(summary = "")

        등 지원하는 애너테이션을 활용해서 조금 더 가독성 있는 API 문서를 만들어 보는 건 어떨까요?
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
