package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.exception.IdCardChangeNotAllowed;
import com.dzajkos.medical_clinic.exception.PatientAlreadyExists;
import com.dzajkos.medical_clinic.exception.PatientNotFound;
import com.dzajkos.medical_clinic.exception.ValueIsNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(PatientNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handlePatientNotFound(PatientNotFound exception) {
        return new ErrorMessage(exception.getMessage(), exception.getStatus(), OffsetDateTime.now());
    }

    @ExceptionHandler(PatientAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handlePatientAlreadyExists(PatientAlreadyExists exception) {
        return new ErrorMessage(exception.getMessage(), exception.getStatus(), OffsetDateTime.now());
    }

    @ExceptionHandler(ValueIsNull.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleValueIsNull(ValueIsNull exception) {
        return new ErrorMessage(exception.getMessage(), exception.getStatus(), OffsetDateTime.now());
    }

    @ExceptionHandler(IdCardChangeNotAllowed.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleIdCardChangeNotAllowed(IdCardChangeNotAllowed exception) {
        return new ErrorMessage(exception.getMessage(), exception.getStatus(), OffsetDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGeneric(Exception exception) {
        return new ErrorMessage("Something went wrong" + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, OffsetDateTime.now());
    }
}
