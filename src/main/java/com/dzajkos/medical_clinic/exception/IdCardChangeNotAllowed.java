package com.dzajkos.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class IdCardChangeNotAllowed extends MedicalClinicException {
    public IdCardChangeNotAllowed(String message, HttpStatus status) {
        super(message, status);
    }
}
