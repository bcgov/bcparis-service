package ca.bc.gov.iamp.bcparis.api.exception.handling;

import ca.bc.gov.iamp.bcparis.api.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "ca.bc.gov.iamp")
public class CustomRestExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    @ExceptionHandler({ConfigurationException.class, ServiceInternalException.class})
    public ResponseEntity<ApiError> handleInternalErrors(Exception ex) {
        return sendError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ObjectNotFoundException.class})
    public ResponseEntity<ApiError> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return sendError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InputValidationException.class, BadRequestException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ApiError> handleBadRequestErrors(Exception ex) {
        return sendError(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ServiceUnavailableException.class})
    public ResponseEntity<ApiError> handleServiceAvailabilityErrors(Exception ex) {
        return sendError(ex, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiError> handleAuthorizationErrors(AccessDeniedException ex) {
        return sendError(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> handleGenericErrors(Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return sendError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
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
}
