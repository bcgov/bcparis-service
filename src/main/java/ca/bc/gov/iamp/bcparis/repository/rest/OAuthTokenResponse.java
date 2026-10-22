package ca.bc.gov.iamp.bcparis.repository.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String toString() {
        return "OAuthTokenResponse{" +
                "accessToken='****MASKED****'" +
                ", expiresIn=" + expiresIn +
                ", scope='" + scope + '\'' +
                '}';
    }
}
