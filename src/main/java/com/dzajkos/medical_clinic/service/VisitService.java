package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.exception.AlreadyAssigned;
import com.dzajkos.medical_clinic.exception.NotFound;
import com.dzajkos.medical_clinic.exception.VisitDateTimeException;
import com.dzajkos.medical_clinic.model.CreateVisitCommand;
import com.dzajkos.medical_clinic.model.Doctor;
import com.dzajkos.medical_clinic.model.Visit;
import com.dzajkos.medical_clinic.repository.DoctorRepository;
import com.dzajkos.medical_clinic.repository.PatientRepository;
import com.dzajkos.medical_clinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import com.dzajkos.medical_clinic.repository.VisitSpecifications;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public Visit addVisit(CreateVisitCommand createVisitCommand) {
        LocalDateTime start = createVisitCommand.getStartDateTime();
        LocalDateTime end = createVisitCommand.getEndDateTime();

        VisitValidator.validateNewVisit(start, end);

        Doctor doctor = doctorRepository.findById(createVisitCommand.getDoctorId())
                .orElseThrow(() -> new NotFound("Doctor with given email does not exist.", HttpStatus.NOT_FOUND));

        if (!visitRepository.findConflictingVisits(doctor.getId(), start, end).isEmpty()) {
            throw new VisitDateTimeException("Doctor already has a visit during this time slot", HttpStatus.CONFLICT);
        }
        Visit visit = Visit.from(start, end, doctor);
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

    public Visit deleteVisit(Long visitID) {
        Visit visit = visitRepository.findById(visitID)
                .orElseThrow(() -> new NotFound("Visit with given ID not found", HttpStatus.NOT_FOUND));
        visitRepository.delete(visit);
        return visit;
    }

    public List<Visit> searchVisits(
            @Nullable LocalDateTime from,
            @Nullable LocalDateTime to,
            @Nullable String specialization,
            boolean availableOnly,
            @Nullable Long patientId,
            @Nullable Long doctorId,
            boolean includePast,
            @Nullable LocalDate day
    ) {
        Specification<Visit> specification = Specification.allOf(
                VisitSpecifications.onDay(day),
                VisitSpecifications.startFrom(day == null ? from : null),
                VisitSpecifications.startTo(day == null ? to : null),
                VisitSpecifications.specialization(specialization),
                VisitSpecifications.availableOnly(availableOnly),
                VisitSpecifications.patientId(patientId),
                VisitSpecifications.doctorId(doctorId),
                VisitSpecifications.excludePast(includePast)
        );

        return visitRepository.findAll(specification);
    }
}
