package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.exception.AlreadyExists;
import com.dzajkos.medical_clinic.exception.NotFound;
import com.dzajkos.medical_clinic.mapper.DoctorMapper;
import com.dzajkos.medical_clinic.model.*;
import com.dzajkos.medical_clinic.repository.ClinicRepository;
import com.dzajkos.medical_clinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final DoctorMapper doctorMapper;

    public PageDTO<DoctorDTO> getDoctors(Pageable pageable) {
        return PageDTO.from(doctorRepository.findAll(pageable), doctorMapper::doctorToDTO);
    }

    public Doctor getDoctor(String email) {
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new NotFound("Doctor with given email does not exist.", HttpStatus.NOT_FOUND));
    }

    public Doctor addDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
            throw new AlreadyExists("Doctor already exists", HttpStatus.CONFLICT);
        }
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(String email) {
        doctorRepository.delete(doctorRepository.findByEmail(email)
                .orElseThrow(() -> new NotFound("Doctor with given email not found", HttpStatus.NOT_FOUND)));
    }

    public Clinic assignClinic(String email, String name) {
       Doctor doctor = doctorRepository.findByEmail(email).orElseThrow(() -> new NotFound("Doctor with given email not found", HttpStatus.NOT_FOUND));
       Clinic clinic = clinicRepository.findByName(name).orElseThrow(() -> new NotFound("Clinic with given name not found", HttpStatus.NOT_FOUND));
       doctor.getClinics().add(clinic);
       doctorRepository.save(doctor);
       return clinic;
    }
}
