package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.exception.PatientAlreadyExists;
import com.dzajkos.medical_clinic.exception.ValueIsNull;
import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.repository.PatientRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PatientValidator {

    public static void nullCheck(Patient patient) {
        if (patient.getPassword() == null ||
                patient.getEmail() == null ||
                patient.getBirthday() == null ||
                patient.getLastName() == null ||
                patient.getFirstName() == null ||
                patient.getPhoneNumber() == null
        ) {
            throw new ValueIsNull("Can't change value to null", HttpStatus.CONFLICT);
        }
    }

    public static void emailCheck(String email, Patient updatedPatient, PatientRepository patientRepository) {
        if (patientRepository.findByEmail(updatedPatient.getEmail()).isPresent()
                && !email.equals(updatedPatient.getEmail())) {
            throw new PatientAlreadyExists("Patient with given email already exists", HttpStatus.CONFLICT);
        }
    }
}
