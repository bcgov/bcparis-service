package ca.bc.gov.iamp.bcparis.filter;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpRequestWrapper copiedRequest = new HttpRequestWrapper((HttpServletRequest) request);

            Layer7Message l7message = new ObjectMapper().readValue(copiedRequest.getRequestBody(), Layer7Message.class);

            if (l7message != null) {
                String corlData = String.format("[messageId:%s | correlationId:%s] ",
                        l7message.getEnvelope().getMqmd().getMessageIdByte(),
                        l7message.getEnvelope().getMqmd().getCorrelationIdByte());

                //Referenced from logging configuration.
                MDC.put("corlData", corlData);
            }

            chain.doFilter(copiedRequest, response);

        } finally {
            MDC.clear();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
