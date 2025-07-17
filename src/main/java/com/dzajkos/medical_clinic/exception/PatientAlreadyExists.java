package com.dzajkos.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class PatientAlreadyExists extends MedicalClinicException {

    public PatientAlreadyExists(String message, HttpStatus status) {
        super(message, status);
    }
}
