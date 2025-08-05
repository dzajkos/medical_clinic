package com.dzajkos.medical_clinic.mapper;

import com.dzajkos.medical_clinic.model.CreateDoctorCommand;
import com.dzajkos.medical_clinic.model.Doctor;
import com.dzajkos.medical_clinic.model.DoctorDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO doctorToDTO(Doctor doctor);
    Doctor createCommandToDoctor(CreateDoctorCommand createDoctorCommand);
}
