package com.dzajkos.medical_clinic.model;

import lombok.*;

import java.time.LocalDate;

@Builder
public record CreatePatientCommand(String email, String password, String idCardNo, String firstName, String lastName,
                                   String phoneNumber, LocalDate birthday) {
}
