package com.dzajkos.medical_clinic.repository;

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

    public void add(Patient patient) {
        patients.add(patient);
    }

    public void update(String email, Patient updatedPatient) {
        delete(email);
        add(updatedPatient);
    }

    public void delete(String email) {
        patients.removeIf(patient -> patient.getEmail().equals(email));
    }
}
