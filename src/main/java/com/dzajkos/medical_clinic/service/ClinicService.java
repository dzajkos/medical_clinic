package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.exception.AlreadyExists;
import com.dzajkos.medical_clinic.exception.NotFound;
import com.dzajkos.medical_clinic.model.Clinic;
import com.dzajkos.medical_clinic.model.Doctor;
import com.dzajkos.medical_clinic.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicRepository clinicRepository;

    public List<Clinic> getClinics() {
        return clinicRepository.findAll();
    }

    public Clinic getClinic(String name) {
        return clinicRepository.findByName(name)
                .orElseThrow(() -> new NotFound("Clinic with given name does not exist.", HttpStatus.NOT_FOUND));
    }

    public Clinic addClinic(Clinic clinic) {
        if (clinicRepository.findByName(clinic.getName()).isPresent()) {
            throw new AlreadyExists("Clinic already exists", HttpStatus.CONFLICT);
        }
        return clinicRepository.save(clinic);
    }

    public void deleteClinic(String name) {
        clinicRepository.delete(clinicRepository.findByName(name)
                .orElseThrow(() -> new NotFound("Clinic with given name not found", HttpStatus.NOT_FOUND)));
    }
}
