package com.example.demo.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("게시판 API")
                .version("1.0.0")
                .description("Spring Boot로 개발한 게시판 프로젝트 API 문서")
                .contact(new Contact()
                        .name("박무근")
                        .email("sohappynow12@naver.com")
                        .url("https://github.com/parkmookeun/InternshipBoard"));


        return new OpenAPI()
                .info(info);
    }
}
