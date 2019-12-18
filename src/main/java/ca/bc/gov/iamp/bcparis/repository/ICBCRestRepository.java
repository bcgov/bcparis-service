package ca.bc.gov.iamp.bcparis.repository;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.MQMD;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.repository.query.IMSResponse;
import ca.bc.gov.iamp.bcparis.repository.rest.BaseRest;

@Repository
public class ICBCRestRepository extends BaseRest {

    private final Logger log = LoggerFactory.getLogger(ICBCRestRepository.class);

    @Value("${endpoint.icbc.rest}")
    private String icbcUrl;

    @Value("${endpoint.icbc.rest.header.imsUserId}")
    private String imsUserId;

    @Value("${endpoint.icbc.rest.header.imsCredential}")
    private String imsCredential;

    @Value("${endpoint.icbc.rest.path.transaction}")
    private String pathTransaction;

    @Value("${endpoint.icbc.rest.header.username}")
    private String username;

    @Value("${endpoint.icbc.rest.header.password}")
    private String password;

    @NewSpan("icbc")
    public String requestDetails(final Layer7Message l7message, IMSRequest ims) {
        try {
            final String URL = icbcUrl + pathTransaction;

            HttpEntity<?> httpEntity = new HttpEntity<IMSRequest>(ims, getHeaders(l7message, username, password));

            ResponseEntity<IMSResponse> response = getRestTemplate().postForEntity(URL, httpEntity, IMSResponse.class);

            assertResponse(HttpStatus.OK, response.getStatusCode(), response.getBody().toString());

            return response.getBody().getImsResponse();
        } catch (HttpServerErrorException e) {
            throw new ICBCRestException(
                    String.format("Message=%s\n Response Body=%s", e.getLocalizedMessage(), e.getResponseBodyAsString()),
                    e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new ICBCRestException(
                    String.format("Message=%s", e.getLocalizedMessage()),
                    e.getLocalizedMessage(), e);
        }
    }

    public HttpHeaders getHeaders(final Layer7Message l7message, final String username, final String password) {
        HttpHeaders headers = getHeadersWithBasicAuth(username, password);
        headers.add("imsUserId", imsUserId);
        headers.add("imsCredential", imsCredential);
        headers.add("auditTransactionId", generateAuditTransactionId(l7message));
        return headers;
    }

    private String generateAuditTransactionId(final Layer7Message l7message) {

        if (l7message != null && l7message.getEnvelope() != null && l7message.getEnvelope().getMqmd() != null) {
            MQMD mqmd = l7message.getEnvelope().getMqmd();
            String messageIdByte = mqmd.getMessageIdByte();
            String correlationIdByte = mqmd.getCorrelationIdByte();
            if (messageIdByte != null && !messageIdByte.isEmpty()) {
                messageIdByte = messageIdByte.replaceAll("\\s+", "");
                return messageIdByte;
            } else if(correlationIdByte !=null && !correlationIdByte.isEmpty()) {
                correlationIdByte = correlationIdByte.replaceAll("\\s+", "");
                return correlationIdByte;
            }

        }
        return UUID.randomUUID().toString();
    }

}
