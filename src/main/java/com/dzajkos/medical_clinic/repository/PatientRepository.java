package com.dzajkos.medical_clinic.repository;

import com.dzajkos.medical_clinic.exception.PatientNotFound;
import com.dzajkos.medical_clinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository {
    private List<Patient> patients = new ArrayList<>();

    public List<Patient> findAll() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> findByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    public Patient add(Patient patient) {
        patients.add(patient);
        return patient;
    }

    public Patient update(String email, Patient updatedPatient) {
        return findByEmail(email)
                .map(originalPatient -> {
                    originalPatient.setPassword(updatedPatient.getPassword());
                    originalPatient.setIdCardNo(updatedPatient.getIdCardNo());
                    originalPatient.setFirstName(updatedPatient.getFirstName());
                    originalPatient.setLastName(updatedPatient.getLastName());
                    originalPatient.setBirthday(updatedPatient.getBirthday());
                    originalPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
                    return originalPatient;
                })
                .orElseThrow(() -> new PatientNotFound("Patient not found"));
    }


    public void delete(String email) {
        Patient patient = findByEmail(email)
                .orElseThrow(() -> new PatientNotFound("Patient not found"));
        patients.remove(patient);
    }
}
