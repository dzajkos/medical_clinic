package com.dzajkos.medical_clinic.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record VisitDTO(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime, DoctorSimpleDTO doctor, PatientDTO patient) {
}
