package com.dzajkos.medical_clinic.model;

import lombok.Builder;

@Builder
public record CreateClinicCommand(String name, String city, String postalCode, String street, String buildingNo) {
}
