package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatient(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
    }

    public void addPatient(Patient patient) {
        patientRepository.add(patient);
    }

    public void updatePatient(String email, Patient updatedPatient) {
        patientRepository.update(email, updatedPatient);
    }

    public void deletePatient(String email) {
        patientRepository.delete(email);
    }
}
