package com.example.onetry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("https://dcu-speakers.store:8888", "https://2024-danpoong-team-33-fe.vercel.app","http://localhost:5137") // 명시적으로 도메인 지정
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowCredentials(true);
    }
}
