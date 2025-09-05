package com.dzajkos.medical_clinic.mapper;

import com.dzajkos.medical_clinic.model.CreatePatientCommand;
import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.model.PatientDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO patientToDTO(Patient patient);
    Patient createCommandToPatient(CreatePatientCommand createPatientCommand);
}
