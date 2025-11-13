package com.dzajkos.medical_clinic.repository;

import com.dzajkos.medical_clinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit> {

    @Query("""
            SELECT v FROM Visit v
            WHERE v.doctor.id = :doctorId
            AND v.endDateTime   > :start
            AND v.startDateTime < :end
            """)
    List<Visit> findConflictingVisits(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
