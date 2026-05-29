package ca.bc.gov.iamp.bcparis.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        try {
            // IMPORTANT: pass wrapped request forward
            filterChain.doFilter(wrappedRequest, response);

        } finally {
            byte[] body = wrappedRequest.getContentAsByteArray();

            if (body.length > 0) {
                String payload = new String(body, wrappedRequest.getCharacterEncoding());
                log.debug("Request body: {}", payload);
            } else {
                log.warn("Request body empty or not readable");
            }
        }
    }
}
