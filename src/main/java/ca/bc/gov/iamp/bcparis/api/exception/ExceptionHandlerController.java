package ca.bc.gov.iamp.bcparis.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.bc.gov.iamp.bcparis.api.exception.ApiRuntimeException;
import ca.bc.gov.iamp.bcparis.api.exception.handling.ApiError;
import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.exception.layer7.Layer7RestException;
import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessage;
import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessageType;
import ca.bc.gov.iamp.bcparis.exception.message.MessageTransformException;
import ca.bc.gov.iamp.bcparis.exception.por.PORRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.service.EmailService;
import ca.bc.gov.iamp.bcparis.util.RequestContext;

@ControllerAdvice
public class ExceptionHandlerController {

	private final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RequestContext context;
	
	@ResponseBody
	@ExceptionHandler({ MessageTransformException.class, InvalidMessageType.class, InvalidMessage.class })
	public ResponseEntity<ApiError> messageException(RuntimeException e) {
		log.error(e.getLocalizedMessage(), e);
		return sendError(e, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<ApiError> sendError(Exception ex, HttpStatus httpStatus) {
        ApiError apiError;
        if (ex instanceof ApiRuntimeException) {
            ApiRuntimeException apiEx = (ApiRuntimeException) ex;
            apiError = new ApiError(httpStatus, apiEx.getLocalizedMessage(), apiEx.getErrors());
        } else {
            apiError = new ApiError(httpStatus, ex.getClass().getName(), ex.getLocalizedMessage());
        }
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
	
	@ResponseBody
	@ExceptionHandler({ ICBCRestException.class, Layer7RestException.class, PORRestException.class })
	public ResponseEntity<Layer7Message> restExceptions(RuntimeException e) {
		log.error(e.getMessage(), e);
		emailService.sendEmail(e.getMessage());
		return new ResponseEntity<Layer7Message>(
			(Layer7Message) context.getRequestObject(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public void satelliteException(Exception e) {
		log.error(e.getMessage(), e);
		emailService.sendEmail(e.getMessage());
	}

}
