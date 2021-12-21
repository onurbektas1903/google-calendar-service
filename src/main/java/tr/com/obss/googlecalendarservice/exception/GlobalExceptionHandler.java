package tr.com.obss.googlecalendarservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import tr.com.common.exceptions.ErrorMessage;
import tr.com.common.exceptions.NotFoundException;
import tr.com.common.exceptions.NotUniqueException;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(
            NotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex), NOT_FOUND);
    }

    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<ErrorMessage> handleNotUniqueException(
            NotUniqueException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex), CONFLICT);
    }
    @ExceptionHandler(GoogleBadRequestException.class)
    public ResponseEntity<ErrorMessage> handleGoogleBadRequestException(
            GoogleBadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex), BAD_REQUEST);
    }
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorMessage> handleNotUniqueException(
//            RuntimeException ex, WebRequest request) {
//        return new ResponseEntity<>(new ErrorMessage(INTERNAL_SERVER_ERROR.value(),Collections.emptySet(),ex.getMessage()),
//                INTERNAL_SERVER_ERROR);
//    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
//            MethodArgumentNotValidException ex, WebRequest request) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return new ResponseEntity<>(new ErrorMessage(new BusinessValidationException(ex.getMessage(),errors)),
//                BAD_REQUEST);
//    }
}