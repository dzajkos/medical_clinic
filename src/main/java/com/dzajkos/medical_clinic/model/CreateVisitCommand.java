package com.dzajkos.medical_clinic.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateVisitCommand {
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
    Long doctorID;
}
