package com.dzajkos.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class AlreadyAssigned extends MedicalClinicException {
    public AlreadyAssigned(String message, HttpStatus status) {
        super(message, status);
    }
}
