package com.onetry.spring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server-url}")
    private String SERVER_URL;
    @Bean
    public OpenAPI springOpenAPI(){

        // API 요청 헤더에 인증 정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("JWT");

        // Security 스키마 설정
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        Server localServer = new Server();
        localServer.setDescription("local server");
        localServer.setUrl("http://localhost:8080/spring");

        Server nginxServer = new Server();
        nginxServer.setDescription("Nginx Proxy Server");
        nginxServer.setUrl("https://goorm-onet.duckdns.org:8888/spring");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("JWT",bearerAuth))
                .addSecurityItem(securityRequirement)
                .servers(List.of(localServer, nginxServer))
                .info(info());
    }

    private Info info(){
        return new Info()
                .title("1try Project")
                .version("0.0.1")
                .description("OAuth2 로그인 하이퍼 링크 입니다:<br><br>"
                        + "[Login with Kakao]("+SERVER_URL + "/oauth2/authorization/kakao)");
    }
}
