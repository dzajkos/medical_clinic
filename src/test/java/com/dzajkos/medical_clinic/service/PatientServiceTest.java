package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.exception.AlreadyExists;
import com.dzajkos.medical_clinic.exception.IdCardChangeNotAllowed;
import com.dzajkos.medical_clinic.exception.NotFound;
import com.dzajkos.medical_clinic.exception.ValueIsNull;
import com.dzajkos.medical_clinic.mapper.PatientMapper;
import com.dzajkos.medical_clinic.model.PageDTO;
import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.model.PatientDTO;
import com.dzajkos.medical_clinic.repository.PatientRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    PatientService patientService;
    PatientRepository patientRepository;
    PatientMapper patientMapper;

    @BeforeEach
    void startUp() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.patientService = new PatientService(patientRepository, patientMapper);
    }

    @Test
    void getPatients_WhenGivenPageable_shouldGetListOfAllPatients() {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        List<Patient> patientList = List.of(
                Patient.builder()
                        .id(1L)
                        .email("jan.kowalski1@example.com")
                        .password("stareHaslo")
                        .idCardNo("ABC123456")
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .phoneNumber("600700800")
                        .birthday(LocalDate.of(1990, 5, 15))
                        .build(),
                Patient.builder()
                        .id(2L)
                        .email("jan.kowalski2@example.com")
                        .password("stareHaslo")
                        .idCardNo("ABC1234567")
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .phoneNumber("600700800")
                        .birthday(LocalDate.of(1990, 5, 15))
                        .build(),
                Patient.builder()
                        .id(3L)
                        .email("jan.kowalski3@example.com")
                        .password("stareHaslo")
                        .idCardNo("ABC123456")
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .phoneNumber("600700800")
                        .birthday(LocalDate.of(1990, 5, 15))
                        .build(),
                Patient.builder()
                        .id(4L)
                        .email("jan.kowalski4@example.com")
                        .password("stareHaslo")
                        .idCardNo("ABC1234567")
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .phoneNumber("600700800")
                        .birthday(LocalDate.of(1990, 5, 15))
                        .build()
        );
        Page<Patient> patientPage = new PageImpl<>(patientList);
        when(patientRepository.findAll(pageable)).thenReturn(patientPage);

        // when
        PageDTO<PatientDTO> result = patientService.getPatients(pageable);

        // then
        Assertions.assertAll(
                () -> Assertions.assertEquals("jan.kowalski4@example.com", result.getContent().getLast().email()),
                () -> Assertions.assertEquals(4, result.getTotalElements())
        );
    }

    @Test
    void getPatient_WhenGivenCorrectEmail_ShouldReturnPatient() {

        // given
        String email = "jan.kowalski1@example.com";
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
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));

        // when
        Patient result = patientService.getPatient(email);

        // then
        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("stareHaslo", result.getPassword())
        );
    }

    @Test
    void getPatient_WhenEmailDoesntExist_ShouldThrowNotFoundException () {
        String email = "NieMaTakiego@example.com";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> patientService.getPatient(email));

        assertAll(
                () -> assertEquals("Patient with given email does not exist.", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception. getStatus())
        );
    }

    @Test
    void addPatient_WhenGivenPatientHasUniqueEmail_shouldAddAndReturnPatient() {

        // given
        Patient patient = Patient.builder()
                .email("jan.kowalski1@example.com")
                .password("stareHaslo")
                .idCardNo("ABC123456")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("600700800")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();
        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // when
        Patient result = patientService.addPatient(patient);

        // then
        assertAll(
                () -> assertEquals("jan.kowalski1@example.com", result.getEmail()),
                () -> assertEquals("ABC123456", result.getIdCardNo()),
                () -> assertEquals("Kowalski", result.getLastName()),
                () -> assertEquals("600700800", result.getPhoneNumber()),
                () -> assertEquals(LocalDate.of(1990, 5, 15), result.getBirthday())
        );
    }

    @Test
    void addPatient_WhenEmailIsTaken_ShouldThrowAlreadyExistsException() {
        Patient patient = Patient.builder()
                .email("jan.kowalski1@example.com")
                .password("stareHaslo")
                .idCardNo("ABC123456")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("600700800")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();
        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.of(patient));

        AlreadyExists exception = assertThrows(AlreadyExists.class, () -> patientService.addPatient(patient));

        assertAll(
                () -> assertEquals("Patient already exists", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getStatus())
        );
    }

    @Test
    void changePassword_WhenGivenCorrectEmailAndPasswordIsNotNull_ShouldUpdatePasswordAndReturnPatient() {
        //given
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
        String email = "jan.kowalski1@example.com";
        String newPassword = "noweHaslo";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // when
        Patient result = patientService.changePassword(email, newPassword);

        // then
        assertAll(
                () -> assertEquals("noweHaslo", result.getPassword()),
                () -> assertEquals(1L, result.getId())
        );
    }

    @Test
    void changePassword_WhenEmailDoesntExist_ShouldThrowNotFoundException() {
        String email = "jan.kowalski1@example.com";
        String newPassword = "noweHaslo";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> patientService.changePassword(email, newPassword));

        assertAll(
                () -> assertEquals("Patient not found", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }

    @Test
    void changePassword_WhenPasswordIsNull_ShouldThrowValueIsNullException() {
        String email = "jan.kowalski1@example.com";
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
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));


        ValueIsNull exception = assertThrows(ValueIsNull.class, () -> patientService.changePassword(email, null));

        assertAll(
                () -> assertEquals("Password is null", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getStatus())
        );
    }

    @Test
    void updatePatient_WhenGivenValidData_shouldUpdatePatient() {
        // given
        String email = "jan.kowalski@example.com";
        Patient originalPatient = Patient.builder()
                .id(1L)
                .email("jan.kowalski@example.com")
                .password("stareHaslo")
                .idCardNo("ABC123456")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("600700800")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();

        Patient updatedPatient = Patient.builder()
                .email("jan.nowak@example.com")
                .password("noweHaslo")
                .idCardNo("ABC123456") // must match original
                .firstName("Janusz")
                .lastName("Kowalski")
                .phoneNumber("600800900")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(originalPatient));
        when(patientRepository.findByEmail(updatedPatient.getEmail())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);

        // when
        Patient result = patientService.updatePatient(email, updatedPatient);

        // then
        assertEquals(updatedPatient.getPassword(), result.getPassword());
        assertEquals(updatedPatient.getEmail(), result.getEmail());
        assertEquals(updatedPatient.getFirstName(), result.getFirstName());
        assertEquals(updatedPatient.getPhoneNumber(), result.getPhoneNumber());
        verify(patientRepository, times(1)).save(originalPatient);
    }

    @Test
    void updatePatient_WhenPatientDoesNotExist_shouldThrowNotFound() {
        // given
        String email = "brak@example.com";
        Patient updatedPatient = Patient.builder()
                .email("jan.nowak@example.com")
                .password("noweHaslo")
                .idCardNo("ABC123456") // must match original
                .firstName("Janusz")
                .lastName("Kowalski")
                .phoneNumber("600800900")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        // then
        NotFound exception = assertThrows(NotFound.class, () -> patientService.updatePatient(email, updatedPatient));

        assertAll(
                () -> assertEquals("Patient with given email does not exist.", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }

    @Test
    void updatePatient_WhenIdCardIsChanged_shouldThrowIdCardChangeNotAllowed() {
        // given
        String email = "jan.kowalski@example.com";
        Patient originalPatient = Patient.builder()
                .id(1L)
                .email("jan.kowalski@example.com")
                .password("stareHaslo")
                .idCardNo("ABC123456")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("600700800")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();

        Patient updatedPatient = Patient.builder()
                .email("jan.nowak@example.com")
                .password("noweHaslo")
                .idCardNo("ABC123456") // must match original
                .firstName("Janusz")
                .lastName("Kowalski")
                .phoneNumber("600800900")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(originalPatient));
        updatedPatient.setIdCardNo("INNY123");

        // then
        IdCardChangeNotAllowed exception = assertThrows(IdCardChangeNotAllowed.class,
                () -> patientService.updatePatient(email, updatedPatient));

        assertAll(
                () -> assertEquals("Can't change ID card number", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getStatus())
        );
    }

    @Test
    void updatePatient_WhenEmailAlreadyInUse_shouldThrowAlreadyExists() {
        // given
        String email = "jan.kowalski@example.com";
        Patient originalPatient = Patient.builder()
                .id(1L)
                .email("jan.kowalski@example.com")
                .password("stareHaslo")
                .idCardNo("ABC123456")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("600700800")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();

        Patient updatedPatient = Patient.builder()
                .email("jan.nowak@example.com")
                .password("noweHaslo")
                .idCardNo("ABC123456") // must match original
                .firstName("Janusz")
                .lastName("Kowalski")
                .phoneNumber("600800900")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();

        Patient anotherPatient = Patient.builder()
                .id(2L)
                .email("istnieje@example.com")
                .idCardNo("XYZ999")
                .password("haslo")
                .firstName("Anna")
                .lastName("Nowak")
                .phoneNumber("500600700")
                .birthday(LocalDate.of(1985, 1, 1))
                .build();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(originalPatient));
        when(patientRepository.findByEmail(updatedPatient.getEmail())).thenReturn(Optional.of(anotherPatient));

        // then
        AlreadyExists exception = assertThrows(AlreadyExists.class,
                () -> patientService.updatePatient(email, updatedPatient));

        assertAll(
                () -> assertEquals("Patient with given email already exists", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getStatus())
        );
    }

    @Test
    void updatePatient_WhenRequiredFieldIsNull_shouldThrowValueIsNull() {
        // given
        String email = "jan.kowalski@example.com";
        Patient originalPatient = Patient.builder()
                .id(1L)
                .email("jan.kowalski@example.com")
                .password("stareHaslo")
                .idCardNo("ABC123456")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("600700800")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();

        Patient updatedPatient = Patient.builder()
                .email("jan.nowak@example.com")
                .password("noweHaslo")
                .idCardNo("ABC123456") // must match original
                .firstName("Janusz")
                .lastName(null)
                .phoneNumber("600800900")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(originalPatient));

        // then
        ValueIsNull exception = assertThrows(ValueIsNull.class,
                () -> patientService.updatePatient(email, updatedPatient));

        assertAll(
                () -> assertEquals("Can't change value to null", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getStatus())
        );
    }

    @Test
    void deletePatient_WhenPatientExists_ShouldDeletePatient() {
        String email = "jan.kowalski@example.com";
        Patient patient = Patient.builder()
                .id(1L)
                .email("jan.kowalski@example.com")
                .password("stareHaslo")
                .idCardNo("ABC123456")
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("600700800")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));

        patientService.deletePatient(email);

        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void deletePatient_WhenPatientDoesntExist_ShouldThrowNotFoundException() {
        String email = "nieMaTakiego@example.com";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> patientService.deletePatient(email));

        assertAll(
                () -> assertEquals("Patient with given email does not exist.", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getStatus())
        );
    }

    @Test
    void batchDeletePatients_WhenGivenPatientSelector_ShouldDeleteSelectedPatients() {
        PatientSelector patientSelector = new PatientSelector(List.of(1L, 2L));

        patientService.batchDeletePatients(patientSelector);

        verify(patientRepository, times(1)).deleteAllById(patientSelector.getPatientIDs());
    }

}
