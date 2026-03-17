package com.jitendra.productservice.exception;

import com.jitendra.productservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {

        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 404),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductExists(ProductAlreadyExistsException ex) {

        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 409),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProduct(InvalidProductException ex) {

        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 400),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InventoryUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleInventoryUnavailable(InventoryUnavailableException ex) {

        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 409),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 500),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}