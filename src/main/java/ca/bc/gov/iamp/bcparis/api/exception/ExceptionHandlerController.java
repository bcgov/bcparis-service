package ca.bc.gov.iamp.bcparis.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.bc.gov.iamp.bcparis.api.response.Error;
import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.exception.layer7.Layer7RestException;
import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessageType;
import ca.bc.gov.iamp.bcparis.exception.message.MessageTransformException;

@ControllerAdvice
public class ExceptionHandlerController {

	private final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ InvalidMessageType.class })
	public Error invalidMessage(InvalidMessageType e) {
		log.error(e.getLocalizedMessage(), e);
		return new Error(e.getMessage());
	}
	
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MessageTransformException.class })
	public Error messageTransformation(MessageTransformException e) {
		log.error(e.getLocalizedMessage(), e);
		return new Error("Error during the message transformation.");
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ ICBCRestException.class, Layer7RestException.class })
	public Error restExceptions(RuntimeException e) {
		log.error(e.getLocalizedMessage(), e);
		return new Error(e.getMessage());
	}
	
}
