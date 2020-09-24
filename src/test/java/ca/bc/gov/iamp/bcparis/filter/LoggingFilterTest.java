package ca.bc.gov.iamp.bcparis.filter;

import ca.bc.gov.iamp.bcparis.model.message.Envelope;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.MQMD;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.DelegatingServletInputStream;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;


public class LoggingFilterTest {

    private static final String MESSAGE_ID_BYTE = "ASTRING";
    private static final String CORRELATION_ID_BYTE = "ANOTHERSTRING";
    @Mock
    HttpServletRequest httpServletRequestMock;

    @Mock
    HttpServletResponse httpServletResponseMock;

    @Mock
    FilterChain filterChainMock;

    @Mock
    FilterConfig filterConfigMock;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
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

        Mockito.when(httpServletRequestMock.getInputStream()).thenReturn(
                new DelegatingServletInputStream(
                        new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(layer7Message))));
        Mockito.when(httpServletRequestMock.getContentType()).thenReturn("application/json");
        Mockito.when(httpServletRequestMock.getCharacterEncoding()).thenReturn("UTF-8");
        Mockito.when(httpServletRequestMock.getRequestURI()).thenReturn("/");
        Mockito.when(httpServletRequestMock.getReader()).thenReturn(new BufferedReader(new StringReader(new ObjectMapper().writeValueAsString(layer7Message))));

        filter.init(filterConfigMock);
        Assertions.assertDoesNotThrow(() -> filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock));
        filter.destroy();

    }

    @Test
    @DisplayName("Test executes with exception handled")
    public void testDoFilterException() throws IOException, ServletException {

        LoggingFilter filter = new LoggingFilter();

        BufferedReader br = new BufferedReader(new StringReader("{'PAINTER':'BOBROSS'}"));
        Mockito.when(httpServletRequestMock.getReader()).thenReturn(br);

        filter.init(filterConfigMock);
        Assertions.assertDoesNotThrow(() -> filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock));
        filter.destroy();
    }
}
