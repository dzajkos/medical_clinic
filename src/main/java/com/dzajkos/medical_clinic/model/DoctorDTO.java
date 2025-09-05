package com.dzajkos.medical_clinic.model;

import lombok.Builder;

import java.util.List;

@Builder
public record DoctorDTO(Long id, String email, String firstName, String lastName, List<ClinicSimpleDTO> clinics) {
}
