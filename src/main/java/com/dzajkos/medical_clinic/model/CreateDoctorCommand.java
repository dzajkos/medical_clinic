package com.dzajkos.medical_clinic.model;

import lombok.Builder;

@Builder
public record CreateDoctorCommand(String email, String password, String firstName, String lastName, String specialization) {
}
