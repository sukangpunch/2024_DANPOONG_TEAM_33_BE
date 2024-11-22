package com.example.onetry.component.url;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlComponent {
    private final HttpServletRequest request;

    @Value("${server_scheme}")
    private String SERVER_SCHEME;

    @Value("${server_host}")
    private String SERVER_HOST;

    @Value("${server_port}")
    private String SERVER_PORT;



    public String makeProfileImgURL(String email) {
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + "/spring" +
                "/users/profile/" + email;
    }

    public String makeCertificationURL(String certificationName) {
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + "/spring" +
                "/certification/download?certificationName=" + certificationName;
    }

    public String makePortfolioURL(String portFolioName) {
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + "/spring" +
                "/portfolio/download?portfolioName=" + portFolioName;
    }

    public String makeIdPhotoURL(Long resumeId) {
        return SERVER_SCHEME+"://"+SERVER_HOST+":"+SERVER_PORT+ "/spring" +
                "/resume/id-photo/" + resumeId;
    }

}
