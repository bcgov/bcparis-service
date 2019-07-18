package ca.bc.gov.iamp.bcparis.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.bc.gov.iamp.bcparis.api.response.Error;
import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessageType;
import ca.bc.gov.iamp.bcparis.exception.message.MessageTransformationException;

@ControllerAdvice
public class ExceptionHandlerController {

//	@ResponseBody
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler({ InvalidMessageException.class })
//	public Error invalidMessage(InvalidMessageException e) {
//		return new Error("Invalid Message.");
//	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ InvalidMessageType.class })
	public Error invalidMessage(InvalidMessageType e) {
		return new Error(e.getMessage());
	}
	
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MessageTransformationException.class })
	public Error messageTransformation(MessageTransformationException e) {
		return new Error("Error during the message transformation.");
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ ICBCRestException.class })
	public Error ICBCException(ICBCRestException e) {
		return new Error(e.getMessage());
	}
	
	
}
