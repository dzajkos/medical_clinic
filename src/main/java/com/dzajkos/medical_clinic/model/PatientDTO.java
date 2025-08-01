package com.dzajkos.medical_clinic.model;

import lombok.*;

import java.time.LocalDate;

@Builder
public record PatientDTO(String email, String firstName, String lastName, String phoneNumber, LocalDate birthday) {
}

