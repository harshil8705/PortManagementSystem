package port.management.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import port.management.system.dto.ExceptionResponse;

@RestControllerAdvice
public class MyGlobalExceptionHandlerClass {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> myResourceNotFoundExceptionHandler(ResourceNotFoundException e) {

        String message = e.getMessage();
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, false);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponse> myAPIException(ApiException e) {

        String message = e.getMessage();
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, false);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

    }

}
