package com.onetry.spring.component.url;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlComponent {
    private final HttpServletRequest request;
    public String makeProfileImgURL(String email) {
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + "/spring" +
                "/users/profile/" + email;
    }
}
