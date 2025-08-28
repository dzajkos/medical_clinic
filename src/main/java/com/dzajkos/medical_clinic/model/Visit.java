package com.dzajkos.medical_clinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
@Entity
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public static Visit from (LocalDateTime start, LocalDateTime end, Doctor doctor) {
        Visit visit = new Visit();
        visit.setStartDateTime(start);
        visit.setEndDateTime(end);
        visit.setDoctor(doctor);
        return visit;
    }

}
