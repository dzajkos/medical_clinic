package com.dzajkos.medical_clinic.exception;

public class PatientAlreadyExists extends RuntimeException {
    public PatientAlreadyExists(String message) {
        super(message);
    }
}
