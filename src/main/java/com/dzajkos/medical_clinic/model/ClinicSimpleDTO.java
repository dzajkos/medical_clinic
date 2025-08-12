package com.dzajkos.medical_clinic.model;

import lombok.Builder;

@Builder
public record ClinicSimpleDTO(Long id, String name, String city, String postalCode, String street, String buildingNo) {
}
