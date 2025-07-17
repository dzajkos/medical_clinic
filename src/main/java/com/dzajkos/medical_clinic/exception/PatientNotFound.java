package com.dzajkos.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class PatientNotFound extends MedicalClinicException {

    public PatientNotFound(String message, HttpStatus status) {
        super(message, status);
    }
}
