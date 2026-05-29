package ca.bc.gov.iamp.bcparis.repository.rest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class IcbcRestLoggingInterceptor
        implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(IcbcRestLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution)
            throws IOException {

        log.debug("===== ICBC HTTP REQUEST (Interceptor) =====");
        log.debug("URI    : {}", request.getURI());
        log.debug("Method : {}", request.getMethod());

        request.getHeaders().forEach((k, v) -> {
            if ("Authorization".equalsIgnoreCase(k) || "loginUserId".equalsIgnoreCase(k)) {
                log.debug("{} : ********", k);
            } else {
                log.debug("{} : {}", k, v);
            }
        });

        log.debug("Body   : {}", new String(body, StandardCharsets.UTF_8));

        ClientHttpResponse response = execution.execute(request, body);

        log.debug("===== ICBC HTTP RESPONSE =====");
        log.debug("Status : {}", response.getStatusCode());
        log.debug("Headers: {}", response.getHeaders());

        return response;
    }
}
