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

    @PostMapping("/{visitID}/book")
    public VisitDTO book(@PathVariable String visitID, @RequestParam String patientID) {
        return visitMapper.visitToDTO(visitService.assignPatient(Long.parseLong(visitID), Long.parseLong(patientID)));
    }

    @DeleteMapping("/{visitID}")
    public VisitDTO deleteVisit(@PathVariable String visitID) {
        return visitMapper.visitToDTO(visitService.deleteVisit(Long.parseLong(visitID)));
    }

    @GetMapping("/search")
    public List<VisitDTO> search(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
            @RequestParam(defaultValue = "false") boolean availableOnly,
            @RequestParam(defaultValue = "true") boolean includePast
    ) {
        return visitService.searchVisits(
                        from, to, specialization, availableOnly,
                        patientId, doctorId, includePast, day
                )
                .stream()
                .map(visitMapper::visitToDTO)
                .toList();
    }
}
