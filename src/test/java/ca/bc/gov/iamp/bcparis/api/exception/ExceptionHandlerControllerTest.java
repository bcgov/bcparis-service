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

import ca.bc.gov.iamp.api.exception.handling.ApiError;
import ca.bc.gov.iamp.bcparis.service.EmailService;

public class ExceptionHandlerControllerTest {

	@InjectMocks
	public ExceptionHandlerController controller;
	
	@Mock
	public EmailService emailService;
	
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
	public void httpServerError_success() {
		ResponseEntity<ApiError> error = controller.httpServerError(new HttpServerErrorException(HttpStatus.NOT_FOUND, "Message"));
		
		Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.anyString());
		Assert.assertEquals("org.springframework.web.client.HttpServerErrorException", error.getBody().getMessage());
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getBody().getStatus());
	}

	@Test
	public void restExceptions_success() {
		ResponseEntity<ApiError> error = controller.restExceptions(new RuntimeException("Message"));
		
		Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.anyString());
		Assert.assertEquals("java.lang.RuntimeException", error.getBody().getMessage());
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getBody().getStatus());
	}

	@Test
	public void satelliteException_success() {
		controller.satelliteException(new Exception("Message"));
		
		Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.anyString());
	}
}
