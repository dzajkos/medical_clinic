package com.dzajkos.medical_clinic.mapper;

import com.dzajkos.medical_clinic.model.CreateVisitCommand;
import com.dzajkos.medical_clinic.model.Visit;
import com.dzajkos.medical_clinic.model.VisitDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    VisitDTO visitToDTO(Visit visit);
    Visit createCommandToVisit (CreateVisitCommand createVisitCommand);
}
