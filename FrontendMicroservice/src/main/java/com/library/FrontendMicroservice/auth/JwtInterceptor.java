package com.library.FrontendMicroservice.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements ClientHttpRequestInterceptor {
    private final JwtCookieManager cookieManager;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        String path = request.getURI().getPath();

        String jwtToken = getJwtFromCurrentRequest();

        if (jwtToken != null && !jwtToken.isEmpty()) {
            request.getHeaders().setBearerAuth(jwtToken);
        }

        return execution.execute(request, body);
    }

    private String getJwtFromCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return cookieManager.getJwtFromRequest(request);
        }

        return null;
    }
}
