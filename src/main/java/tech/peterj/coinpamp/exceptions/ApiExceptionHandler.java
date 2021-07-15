package tech.peterj.coinpamp.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Logger;

@ControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(ApiExceptionHandler.class.getName());

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception e) {
        LOGGER.severe(e.getMessage());
        e.printStackTrace();

        var response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Oops! Something went wrong..."
        );

        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    static class ErrorResponse {

        private final HttpStatus status;
        private final String message;

        public ErrorResponse(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
