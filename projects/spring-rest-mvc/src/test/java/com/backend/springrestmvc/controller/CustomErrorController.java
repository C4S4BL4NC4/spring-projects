package com.backend.springrestmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler
    ResponseEntity handleJPAViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if (exception.getCause() instanceof ConstraintViolationException) {
            var ve = (ConstraintViolationException) exception.getCause().getCause();
            var errors = ve.getConstraintViolations().stream()
                    .map(constraintViolation -> {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(
                                constraintViolation.getPropertyPath().toString(),
                                constraintViolation.getMessage()
                        );
                        return errorMap;
                    }).toList();
            return responseEntity.body(errors);
        }
        return responseEntity.build();
    }
}
