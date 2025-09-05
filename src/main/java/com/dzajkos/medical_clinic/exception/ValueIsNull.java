package com.dzajkos.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class ValueIsNull extends MedicalClinicException {

    public ValueIsNull(String message, HttpStatus status) {
        super(message, status);
    }
}
