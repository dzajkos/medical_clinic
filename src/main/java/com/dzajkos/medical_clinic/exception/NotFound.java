package com.dzajkos.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class NotFound extends MedicalClinicException {

    public NotFound(String message, HttpStatus status) {
        super(message, status);
    }
}
