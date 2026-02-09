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

        log.info("===== ICBC HTTP REQUEST (Interceptor) =====");
        log.info("URI    : {}", request.getURI());
        log.info("Method : {}", request.getMethod());

        request.getHeaders().forEach((k, v) -> {
            if ("Authorization".equalsIgnoreCase(k)) {
                log.info("{} : Basic ********", k);
            } else {
                log.info("{} : {}", k, v);
            }
        });

        log.info("Body   : {}", new String(body, StandardCharsets.UTF_8));

        ClientHttpResponse response = execution.execute(request, body);

        log.info("===== ICBC HTTP RESPONSE =====");
        log.info("Status : {}", response.getStatusCode());
        log.info("Headers: {}", response.getHeaders());

        return response;
    }
}
