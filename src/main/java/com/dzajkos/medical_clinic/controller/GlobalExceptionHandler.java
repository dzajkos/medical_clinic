package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicalClinicException.class)
    public ResponseEntity<ErrorMessage> handleMedicalClinicException(MedicalClinicException exception) {
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage(), exception.getStatus(), OffsetDateTime.now()), exception.getStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGeneric(Exception exception) {
        return new ErrorMessage("Something went wrong" + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, OffsetDateTime.now());
    }
}
