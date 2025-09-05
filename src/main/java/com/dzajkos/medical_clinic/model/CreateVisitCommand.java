package com.dzajkos.medical_clinic.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateVisitCommand {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long doctorId;
}
