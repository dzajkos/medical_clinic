package com.dzajkos.medical_clinic.exception;

public class IdCardChangeNotAllowed extends RuntimeException {
    public IdCardChangeNotAllowed(String message) {
        super(message);
    }
}
