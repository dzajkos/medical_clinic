package com.dzajkos.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExists extends MedicalClinicException {

    public AlreadyExists(String message, HttpStatus status) {
        super(message, status);
    }
}
