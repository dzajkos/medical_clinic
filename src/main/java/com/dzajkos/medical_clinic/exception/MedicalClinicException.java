package com.dzajkos.medical_clinic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MedicalClinicException extends RuntimeException {
    public HttpStatus status;

    public MedicalClinicException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
