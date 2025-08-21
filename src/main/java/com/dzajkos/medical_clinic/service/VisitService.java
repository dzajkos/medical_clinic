package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.exception.AlreadyAssigned;
import com.dzajkos.medical_clinic.exception.NotFound;
import com.dzajkos.medical_clinic.model.CreateVisitCommand;
import com.dzajkos.medical_clinic.model.Doctor;
import com.dzajkos.medical_clinic.model.Visit;
import com.dzajkos.medical_clinic.repository.DoctorRepository;
import com.dzajkos.medical_clinic.repository.PatientRepository;
import com.dzajkos.medical_clinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public Visit addVisit(CreateVisitCommand createVisitCommand) {
        LocalDateTime start = createVisitCommand.getStartDateTime();
        LocalDateTime end = createVisitCommand.getEndDateTime();

        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Visit can't be set in the past");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Visit end time must be after start time");
        }
        if (start.getMinute() % 15 != 0) {
            throw new IllegalArgumentException("Visit time can only be set in equal 15 minute intervals");
        }

        Doctor doctor = doctorRepository.findById(createVisitCommand.getDoctorID())
                .orElseThrow(() -> new NotFound("Doctor with given email does not exist.", HttpStatus.NOT_FOUND));

        if (doctor.hasConflictingVisit(start, end)) {
            throw new IllegalStateException("Doctor already has a visit during this time slot");
        }
        Visit visit = new Visit();
        visit.setStartDateTime(start);
        visit.setEndDateTime(end);
        visit.setDoctor(doctor);
        return visitRepository.save(visit);
    }

    public Visit assignPatient(Long visitID, Long patientID) {
        Visit visit = visitRepository.findById(visitID)
                .orElseThrow(() -> new NotFound("Visit with given ID not found",  HttpStatus.NOT_FOUND));
        if (visit.getPatient() != null) {
            throw new AlreadyAssigned("A Patient is already assigned to this visit", HttpStatus.CONFLICT);
        }
        visit.setPatient(patientRepository.findById(patientID)
                .orElseThrow(() -> new NotFound("Patient with given ID not found", HttpStatus.NOT_FOUND)));
        visitRepository.save(visit);
        return visit;
    }
}
