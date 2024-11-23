package com.example.onetry.config;

import com.example.onetry.component.converter.OctetStreamReadMsgConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private OctetStreamReadMsgConverter octetStreamReadMsgConverter;

    @Autowired
    public WebConfig(OctetStreamReadMsgConverter octetStreamReadMsgConverter) {
        this.octetStreamReadMsgConverter = octetStreamReadMsgConverter;
    }

    // HttpMessageConverter는 HTTP 요청 본문 또는 응답 본문을 Java 객체로 변환하거나 그 반대로 변환하는 역할
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(octetStreamReadMsgConverter); // 사용자 정의 변환기로, 이 변환기를 converters 리스트에 추가하여 Spring이 해당 변환기를 사용할 수 있도록 한다.
    }
}
