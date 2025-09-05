package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.model.CreateVisitCommand;
import com.dzajkos.medical_clinic.model.Doctor;
import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.model.Visit;
import com.dzajkos.medical_clinic.repository.DoctorRepository;
import com.dzajkos.medical_clinic.repository.PatientRepository;
import com.dzajkos.medical_clinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class VisitServiceTest {

    VisitRepository visitRepository;
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;
    VisitService visitService;

    @BeforeEach
    void StartUp() {
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.visitService = new VisitService(visitRepository, doctorRepository, patientRepository);
    }

    @Test
    void addVisit_WhenGivenCorrectDoctorIdAndDoctorDoesntHaveAnyVisitsAtSameTime_ShouldAddVisit() {
        CreateVisitCommand createVisitCommand = CreateVisitCommand.builder()
                .startDateTime(LocalDateTime.now().withMinute(0).plusHours(2))
                .endDateTime(LocalDateTime.now().withMinute(0).plusHours(3))
                .doctorId(1L)
                .build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .email("jan.kowalski1@example.com")
                .password("stareHaslo")
                .firstName("Jan")
                .lastName("Kowalski")
                .clinics(new ArrayList<>())
                .build();
        when(doctorRepository.findById(createVisitCommand.getDoctorId())).thenReturn(Optional.of(doctor));
        when(visitRepository.findConflictingVisits(any(), any(), any())).thenReturn(new ArrayList<>());
        when(visitRepository.save(any(Visit.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        Visit result = visitService.addVisit(createVisitCommand);

        // then
        assertAll(
                () -> assertEquals(1L, result.getDoctor().getId()),
                () -> assertEquals(createVisitCommand.getStartDateTime(), result.getStartDateTime()),
                () -> assertEquals(createVisitCommand.getEndDateTime(), result.getEndDateTime())
        );
    }

    @Test
    void assignPatient_WhenGivenCorrectVisitIdAndPatientIdAndVisitDoesntHaveAnyPatientAssigned_ShouldAssignPatientToVisit() {
        Long visitID = 1L;
        Long patientID = 1L;
        Patient patient = Patient.builder()
                .id(1L)
                .email("jan.kowalski1@example.com")
                .password("stareHaslo")
                .idCardNo("ABC123456")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("600700800")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();
        Visit visit = Visit.builder()
                .id(1L)
                .startDateTime(LocalDateTime.now().withMinute(0).plusHours(2))
                .endDateTime(LocalDateTime.now().withMinute(0).plusHours(3))
                .doctor(Doctor.builder()
                        .id(1L)
                        .email("jan.kowalski1@example.com")
                        .password("stareHaslo")
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .clinics(new ArrayList<>())
                        .build())
                .build();
        when(visitRepository.findById(visitID)).thenReturn(Optional.of(visit));
        when(patientRepository.findById(patientID)).thenReturn(Optional.of(patient));
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        // when
        Visit result = visitService.assignPatient(visitID, patientID);

        // then

        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals(visit.getStartDateTime(), result.getStartDateTime()),
                () -> assertEquals(visit.getEndDateTime(), result.getEndDateTime()),
                () -> assertEquals(patient.getId(), result.getPatient().getId()),
                () -> assertEquals(patient, result.getPatient())

        );

    }
}
