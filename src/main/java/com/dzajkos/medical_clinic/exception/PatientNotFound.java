package com.dzajkos.medical_clinic.exception;

public class PatientNotFound extends RuntimeException {
    public PatientNotFound(String message) {
        super(message);
    }
}
