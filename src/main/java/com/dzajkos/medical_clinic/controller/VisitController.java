package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.mapper.VisitMapper;
import com.dzajkos.medical_clinic.model.CreateVisitCommand;
import com.dzajkos.medical_clinic.model.Visit;
import com.dzajkos.medical_clinic.model.VisitDTO;
import com.dzajkos.medical_clinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @PutMapping
    public VisitDTO assignPatient(String visitID, String patientID) {
        return visitMapper.visitToDTO(visitService.assignPatient(Long.parseLong(visitID), Long.parseLong(patientID)));
    }

    @GetMapping
    @RequestMapping("/patient/own")
    public List<VisitDTO> getPatientOwnVisitList(String patientID) {
        return visitService.getVisitsListOfPatient(Long.parseLong(patientID))
                .stream()
                .map(visitMapper::visitToDTO)
                .toList();
    }

    @GetMapping
    @RequestMapping("/patient/doctor")
    public List<VisitDTO> getDoctorVisitList(String doctorID) {
        return visitService.getVisitsListOfDoctor(Long.parseLong(doctorID))
                .stream()
                .map(visitMapper::visitToDTO)
                .toList();
    }

    @GetMapping
    @RequestMapping("/patient/specialization")
    public List<VisitDTO> getSpecializationVisitList(
            String specialization,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return visitService.getVisitsForSpecialization(specialization, date)
                .stream()
                .map(visitMapper::visitToDTO)
                .toList();
    }

    @GetMapping
    @RequestMapping("/doctor/own")
    public List<VisitDTO> getDoctorOwnVisitList(String doctorID) {
        return  visitService.getOwnVisitsListOfDoctor(Long.parseLong(doctorID))
                .stream()
                .map(visitMapper::visitToDTO)
                .toList();
    }

    @DeleteMapping
    @RequestMapping("/doctor")
    public VisitDTO deleteVisit(String visitID) {
        return visitMapper.visitToDTO(visitService.deleteVisit(Long.parseLong(visitID)));
    }

    @GetMapping("/search")
    public List<VisitDTO> search(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String specialization,
            @RequestParam(defaultValue = "false") boolean availableOnly) {

        return visitService.searchVisits(from, to, specialization, availableOnly)
                .stream()
                .map(visitMapper::visitToDTO)
                .toList();
    }
}
