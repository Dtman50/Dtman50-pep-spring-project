package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAndErrorController {

    @ExceptionHandler(DuplicateAccountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleDuplicateAccount(DuplicateAccountException de) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(de.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleException(Exception re) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(re.getMessage());
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleUnautorizedUser(UnauthorizedUserException ue) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ue.getMessage());
    }
    
}
