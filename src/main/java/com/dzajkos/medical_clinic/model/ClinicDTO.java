package com.dzajkos.medical_clinic.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ClinicDTO(Long id, String name, String city, String postalCode, String street, String buildingNo, List<DoctorDTO> doctors) {
}
