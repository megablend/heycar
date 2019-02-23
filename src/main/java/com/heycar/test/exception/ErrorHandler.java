/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.exception;
import com.heycar.test.dto.ErrorDetail;
import java.util.Date;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author Charles Megafu
 */
@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetail> handleGlobalException(Exception ex, WebRequest request) {
      ErrorDetail errorDetails;
      
      if (ex instanceof ConstraintViolationException) {
          Set<ConstraintViolation<?>> constraintViolation = ((ConstraintViolationException) ex).getConstraintViolations();
          Set<String> errors = constraintViolation.stream().map(v -> String.format("%s, %s", v.getInvalidValue(), v.getMessage())).collect(toSet());
          errorDetails = new ErrorDetail(new Date(), "Invalid Parameter(s) Provided", errors);
      } else 
          errorDetails = new ErrorDetail(new Date(), ex.getMessage(), request.getDescription(false));
      return new ResponseEntity<>(errorDetails, HttpStatus.BAD_GATEWAY);
    }
}
