package ca.bc.gov.iamp.bcparis.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.util.RequestContext;


@Component
public class LoggingFilter  implements Filter {

	@Autowired
	private RequestContext context;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
        	Layer7Message l7message = (Layer7Message) context.getRequestObject();
        	
        	if(l7message != null) {
        		String mdcData = String.format("[messageIdByte:%s | correlationIdByte:%s] ", 
            			l7message.getEnvelope().getMqmd().getMessageIdByte(), 
            			l7message.getEnvelope().getMqmd().getCorrelationIdByte());
            	
            	//Referenced from logging configuration.
                MDC.put("mdcData", mdcData);
        	}

        	chain.doFilter(request, response);
           
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
