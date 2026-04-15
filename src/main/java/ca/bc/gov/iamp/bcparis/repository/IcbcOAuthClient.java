package ca.bc.gov.iamp.bcparis.repository;

import ca.bc.gov.iamp.bcparis.repository.rest.OAuthTokenResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Component
public class IcbcOAuthClient {

    private static final Logger log = LoggerFactory.getLogger(IcbcOAuthClient.class);

    private final RestTemplate restTemplate;

    @Value("${icbc.oauth.token-url}")
    private String tokenUrl;

    @Value("${icbc.oauth.client-id}")
    private String clientId;

    @Value("${icbc.oauth.client-secret}")
    private String clientSecret;

    @Value("${icbc.oauth.scope}")
    private String scope;

    // ---- Cached token state ----
    private volatile String cachedAccessToken;
    private volatile Instant tokenExpiryTime;

    // Refresh token 60 seconds before actual expiry
    private static final long EXPIRY_BUFFER_SECONDS = 60;

    public IcbcOAuthClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public synchronized String getAccessToken() {

        if (isTokenValid()) {
            log.debug("Using cached ICBC OAuth token");
            return cachedAccessToken;
        }

        log.info("Cached token missing or expired. Requesting new ICBC OAuth token.");
        return requestNewToken();
    }

    /**
     * Invalidates the cached token, forcing a new token request on next getAccessToken() call
     */
    public synchronized void invalidateToken() {
        log.info("Invalidating cached OAuth token");
        this.cachedAccessToken = null;
        this.tokenExpiryTime = null;
    }

    private String requestNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", "client_credentials");
        body.add("scope", scope);

        log.debug(
                "OAuth token request params: client_id={}, grant_type=client_credentials, scope={}",
                clientId,
                scope);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<OAuthTokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                OAuthTokenResponse.class);

        OAuthTokenResponse tokenResponse = response.getBody();

        log.info("OAuthTokenResponse: {}", tokenResponse);

        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new IllegalStateException("Failed to retrieve ICBC OAuth token");
        }

        cacheToken(tokenResponse);

        log.info("ICBC OAuth token retrieved and cached");
        log.debug("Token expires in {} seconds", tokenResponse.getExpiresIn());

        return cachedAccessToken;
    }

    private boolean isTokenValid() {
        return cachedAccessToken != null
                && tokenExpiryTime != null
                && Instant.now().isBefore(tokenExpiryTime);
    }

    private void cacheToken(OAuthTokenResponse tokenResponse) {
        this.cachedAccessToken = tokenResponse.getAccessToken();

        long expiresIn = tokenResponse.getExpiresIn();
        this.tokenExpiryTime = Instant.now()
                .plusSeconds(expiresIn - EXPIRY_BUFFER_SECONDS);

        log.debug("Token cached until {}", tokenExpiryTime);
    }
}
