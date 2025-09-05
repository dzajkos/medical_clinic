package com.dzajkos.medical_clinic.model;

import lombok.Builder;

import java.util.List;

@Builder
public record DoctorSimpleDTO(Long id, String email, String firstName, String lastName) {
}

