package ca.bc.gov.iamp.bcparis.api.exception;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import ca.bc.gov.iamp.bcparis.api.exception.handling.ApiError;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.service.EmailService;
import ca.bc.gov.iamp.bcparis.util.RequestContext;
import test.util.BCPARISTestUtil;

public class ExceptionHandlerControllerTest {

	@InjectMocks
	public ExceptionHandlerController controller;
	
	@Mock
	public EmailService emailService;
	
	@Mock
	private RequestContext context;
	
	@Before
    public void initMocks() throws NoSuchFieldException, SecurityException{
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void messageException_success() {
		ResponseEntity<ApiError> error = controller.messageException(new RuntimeException("Message"));
		
		Assert.assertEquals("java.lang.RuntimeException", error.getBody().getMessage());
		Assert.assertEquals("Message", error.getBody().getErrors().get(0));
		Assert.assertEquals(HttpStatus.BAD_REQUEST, error.getBody().getStatus());
	}

	@Test
	public void restExceptions_success() {
		final Layer7Message driverSNME = BCPARISTestUtil.getMessageDriverSNME();
		final String content = driverSNME.getEnvelope().getBody().getMsgFFmt(); 
				
		Mockito.when(context.getRequestObject()).thenReturn(driverSNME);
		
		ResponseEntity<Layer7Message> message = controller.restExceptions(new RuntimeException("Message"));
		
		Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.anyString());
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, message.getStatusCode());
		Assert.assertEquals(content, message.getBody().getEnvelope().getBody().getMsgFFmt());
	}

	@Test
	public void satelliteException_success() {
		controller.satelliteException(new Exception("Message"));
		Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.anyString());
	}
}
