package com.dzajkos.medical_clinic.mapper;

import com.dzajkos.medical_clinic.model.CreateClinicCommand;
import com.dzajkos.medical_clinic.model.Clinic;
import com.dzajkos.medical_clinic.model.ClinicDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
    ClinicDTO clinicToDTO(Clinic clinic);
    Clinic createCommandToClinic(CreateClinicCommand createClinicCommand);
}
