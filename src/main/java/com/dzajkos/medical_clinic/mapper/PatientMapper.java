package com.dzajkos.medical_clinic.mapper;

import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.model.PatientCreationDTO;
import com.dzajkos.medical_clinic.model.PatientResponseDTO;

public class PatientMapper {

    public static Patient toEntity(PatientCreationDTO dto) {
        Patient patient = new Patient();
        patient.setEmail(dto.getEmail());
        patient.setPassword(dto.getPassword());
        patient.setIdCardNo(dto.getIdCardNo());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setBirthday(dto.getBirthday());
        return patient;
    }

    public static PatientResponseDTO toResponseDTO(Patient patient) {
        return new PatientResponseDTO(
            patient.getEmail(),
            patient.getFirstName(),
            patient.getLastName(),
            patient.getPhoneNumber(),
            patient.getBirthday()
        );
    }
}
