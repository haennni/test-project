package sample.cafekiosk.spring.domain.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException ex) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult()
                        .getAllErrors().get(0)
                        .getDefaultMessage());
    }
}
