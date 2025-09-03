package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.mapper.VisitMapper;
import com.dzajkos.medical_clinic.model.CreateVisitCommand;
import com.dzajkos.medical_clinic.model.Visit;
import com.dzajkos.medical_clinic.model.VisitDTO;
import com.dzajkos.medical_clinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visit")
public class VisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    public VisitDTO addVisit(@RequestBody CreateVisitCommand createVisitCommand) {
        Visit visit = visitService.addVisit(createVisitCommand);
        return visitMapper.visitToDTO(visit);
    }

    @PatchMapping
    public VisitDTO assignPatient(String visitID, String patientID) {
        return visitMapper.visitToDTO(visitService.assignPatient(Long.parseLong(visitID), Long.parseLong(patientID)));
    }
}
