package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.exception.NotFound;
import com.dzajkos.medical_clinic.mapper.DoctorMapper;
import com.dzajkos.medical_clinic.model.*;
import com.dzajkos.medical_clinic.model.Doctor;
import com.dzajkos.medical_clinic.repository.ClinicRepository;
import com.dzajkos.medical_clinic.repository.DoctorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {

    DoctorRepository doctorRepository;
    ClinicRepository clinicRepository;
    DoctorMapper doctorMapper;
    DoctorService doctorService;

    @BeforeEach
    void StartUp() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.clinicRepository = Mockito.mock(ClinicRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.doctorService = new DoctorService(doctorRepository, clinicRepository, doctorMapper);
    }

    @Test
    void getDoctors_WhenGivenPageable_ShouldReturnPageDTOofDoctorDTO() {
        // given
        Pageable pageable = PageRequest.of(0, 2);
        List<Doctor> doctorList = List.of(
                Doctor.builder()
                        .id(1L)
                        .email("jan.kowalski1@example.com")
                        .password("stareHaslo")
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .build(),
                Doctor.builder()
                        .id(2L)
                        .email("jan.kowalski2@example.com")
                        .password("stareHaslo")
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .build(),
                Doctor.builder()
                        .id(3L)
                        .email("jan.kowalski3@example.com")
                        .password("stareHaslo")
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .build(),
                Doctor.builder()
                        .id(4L)
                        .email("jan.kowalski4@example.com")
                        .password("stareHaslo")
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .build()
        );
        Page<Doctor> doctorPage = new PageImpl<Doctor>(doctorList);
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);

        // when
        PageDTO<DoctorDTO> result = doctorService.getDoctors(pageable);

        // then
        Assertions.assertAll(
                () -> Assertions.assertEquals("jan.kowalski4@example.com", result.getContent().getLast().email()),
                () -> Assertions.assertEquals(4, result.getTotalElements())
        );
    }

    @Test
    void getDoctor_WhenGivenCorrectEmail_ShouldReturnDoctor() {

        // given
        String email = "jan.kowalski1@example.com";
        Doctor doctor = Doctor.builder()
                .id(1L)
                .email("jan.kowalski1@example.com")
                .password("stareHaslo")
                .firstName("Jan")
                .lastName("Kowalski")
                .build();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));

        // when
        Doctor result = doctorService.getDoctor(email);

        // then
        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("stareHaslo", result.getPassword())
        );
    }

    @Test
    void addDoctor_WhenGivenDoctorHasUniqueEmail_shouldAddAndReturnDoctor() {

        // given
        Doctor doctor = Doctor.builder()
                .email("jan.kowalski1@example.com")
                .password("stareHaslo")
                .firstName("Jan")
                .lastName("Kowalski")
                .build();
        when(doctorRepository.findByEmail(doctor.getEmail())).thenReturn(Optional.empty());
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        Doctor result = doctorService.addDoctor(doctor);

        // then
        assertAll(
                () -> assertEquals("jan.kowalski1@example.com", result.getEmail()),
                () -> assertEquals("Kowalski", result.getLastName())
        );
    }

    @Test
    void assignClinic_WhenGivenCorrectEmailAndClinicName_ShouldAddClinicToDoctor() {
        String email = "jan.kowalski1@example.com";
        String name = "clinicName";
        Doctor doctor = Doctor.builder()
                .id(1L)
                .email("jan.kowalski1@example.com")
                .password("stareHaslo")
                .firstName("Jan")
                .lastName("Kowalski")
                .clinics(new ArrayList<>())
                .build();
        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("clinicName")
                .city("clinicCity")
                .postalCode("01-001")
                .street("clinicStreet")
                .buildingNo("10/15")
                .build();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(clinicRepository.findByName(name)).thenReturn(Optional.of(clinic));
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        Clinic result = doctorService.assignClinic(email, name);

        // then
        assertAll(
                () -> assertEquals("clinicName", result.getName()),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("clinicName", doctor.getClinics().getFirst().getName()),
                () -> assertEquals(1L, doctor.getClinics().getFirst().getId())
        );
    }

    @Test
    void deleteDoctor_WhenDoctorExists_ShouldDeleteDoctor() {
        String email = "doktorKowalski@example.com";
        Doctor doctor = Doctor.builder()
                .id(1L)
                .email("doktorKowalski@example.com")
                .firstName("Jan")
                .lastName("Kowalski")
                .build();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(email);

        verify(doctorRepository, times(1)).delete(doctor);
    }

    @Test
    void deleteDoctor_WhenDoctorDoesntExist_ShouldThrowNotFoundException() {
        String email = "nieMaTakiego@example.com";
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> doctorService.deleteDoctor(email));

        assertAll(
                () -> assertEquals("Doctor with given email not found", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }
}
