package ca.bc.gov.iamp.bcparis.filter;

import ca.bc.gov.iamp.bcparis.model.message.Envelope;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.MQMD;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoggingFilterTest {

    private static final String MESSAGE_ID_BYTE = "ASTRING";
    private static final String CORRELATION_ID_BYTE = "ANOTHERSTRING";


    MockHttpServletRequest httpServletRequestMock = new MockHttpServletRequest();

    HttpServletResponse httpServletResponseMock = new MockHttpServletResponse();

    @Mock
    FilterChain filterChainMock;

    @Mock
    FilterConfig filterConfigMock;

    @Before
    public void inti(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Test executes with no exception")
    public void testDoFilter() throws ServletException, IOException {

        LoggingFilter filter = new LoggingFilter();

        Layer7Message layer7Message = new Layer7Message();
        MQMD mqmd = new MQMD();
        mqmd.setMessageIdByte(MESSAGE_ID_BYTE);
        mqmd.setCorrelationIdByte(CORRELATION_ID_BYTE);

        Envelope envelope = new Envelope();
        envelope.setMqmd(mqmd);

        layer7Message.setEnvelope(envelope);

        filter.init(filterConfigMock);
        Assertions.assertDoesNotThrow(() -> filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock));
        filter.destroy();

    }

    @Test
    @DisplayName("Test executes with exception handled")
    public void testDoFilterException() throws ServletException {

        LoggingFilter filter = new LoggingFilter();

        filter.init(filterConfigMock);
        Assertions.assertDoesNotThrow(() -> filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock));
        filter.destroy();
    }
}
