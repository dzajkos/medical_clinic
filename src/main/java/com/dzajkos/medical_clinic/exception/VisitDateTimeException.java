package com.dzajkos.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class VisitDateTimeException extends MedicalClinicException {
    public VisitDateTimeException(String message, HttpStatus status) {
        super(message, status);
    }
}
