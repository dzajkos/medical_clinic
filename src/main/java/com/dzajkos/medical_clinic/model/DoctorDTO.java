package com.dzajkos.medical_clinic.model;

import lombok.Builder;

@Builder
public record DoctorDTO(Long id, String email, String firstName, String lastName) {
}
