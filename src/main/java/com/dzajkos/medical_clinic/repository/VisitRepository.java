package com.dzajkos.medical_clinic.repository;

import com.dzajkos.medical_clinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("SELECT v FROM Visit v " +
            "WHERE v.doctor.id = :doctorId " +
            "AND v.endDateTime > :start " +
            "AND v.startDateTime < :end")
    List<Visit> findConflictingVisits(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<Visit> findAllByPatientId(Long patientID);
    List<Visit> findAllByDoctorIdAndPatientIsNull(Long doctorID);

    @Query("""
        SELECT v FROM Visit v
        WHERE v.patient IS NULL
        AND LOWER(v.doctor.specialization) = LOWER(:specialization)
        AND v.startDateTime >= :start
        AND v.startDateTime < :end
        """)
    List<Visit> findAvailableBySpecAndStartBetween(
            @Param("specialization") String specialization,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<Visit> findAllByDoctorId(Long doctorID);

    @Query("""
            SELECT v FROM Visit v
            WHERE v.startDateTime < :endAt
            AND v.endDateTime   > :startAt
            AND (:spec IS NULL OR LOWER(v.doctor.specialization)=LOWER(:spec))
            AND (:availableOnly = FALSE OR v.patient IS NULL)
            """)
    List<Visit> search(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt")   LocalDateTime endAt,
            @Param("spec")    String specialization,
            @Param("availableOnly") boolean availableOnly
    );
}

