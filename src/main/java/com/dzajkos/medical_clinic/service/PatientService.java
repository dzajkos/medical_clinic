package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.exception.IdCardChangeNotAllowed;
import com.dzajkos.medical_clinic.exception.PatientAlreadyExists;
import com.dzajkos.medical_clinic.exception.PatientNotFound;
import com.dzajkos.medical_clinic.exception.ValueIsNull;
import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() -> new PatientNotFound("Patient with given email does not exist.", HttpStatus.NOT_FOUND));
    }

    public Patient addPatient(Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new PatientAlreadyExists("Patient already exists", HttpStatus.CONFLICT);
        }
        return patientRepository.save(patient);
    }

    public Patient updatePatient(String email, Patient updatedPatient) {
        Patient originalPatient = getPatient(email);
        if (!updatedPatient.getIdCardNo().equals(originalPatient.getIdCardNo())) {
            throw new IdCardChangeNotAllowed("Can't change ID card number", HttpStatus.CONFLICT);
        }
        PatientValidator.nullCheck(updatedPatient);

        PatientValidator.emailCheck(email, updatedPatient, patientRepository);

        originalPatient.setPassword(updatedPatient.getPassword());
        originalPatient.setEmail(updatedPatient.getEmail());
        originalPatient.setBirthday(updatedPatient.getBirthday());
        originalPatient.setFirstName(updatedPatient.getFirstName());
        originalPatient.setLastName(updatedPatient.getLastName());
        originalPatient.setPhoneNumber(updatedPatient.getPhoneNumber());

        return patientRepository.save(originalPatient);
    }

    public void deletePatient(String email) {
        patientRepository.delete(getPatient(email));
    }

    public Patient changePassword(String email, String newPassword) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFound("Patient not found", HttpStatus.NOT_FOUND));
        if (newPassword == null) {
            throw new ValueIsNull("Password is null", HttpStatus.CONFLICT);
        }
        patient.setPassword(newPassword);
        return patientRepository.save(patient);
    }
}
