package com.dzajkos.medical_clinic.repository;

import com.dzajkos.medical_clinic.model.Visit;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class VisitSpecifications {
    private VisitSpecifications() {}

    public static Specification<Visit> patientId(Long patientId) {
        return (root, query, criteriaBuilder) ->
                patientId == null
                        ? null
                        : criteriaBuilder.equal(root.get("patient").get("id"), patientId);
    }

    public static Specification<Visit> doctorId(Long doctorId) {
        return (root, query, criteriaBuilder) ->
                doctorId == null
                        ? null
                        : criteriaBuilder.equal(root.get("doctor").get("id"), doctorId);
    }

    public static Specification<Visit> specialization(String specialization) {
        return (root, query, criteriaBuilder) ->
                (specialization == null || specialization.isBlank())
                        ? null
                        : criteriaBuilder.equal(root.get("doctor").get("specialization"), specialization);
    }

    public static Specification<Visit> availableOnly(Boolean availableOnly) {
        return (root, query, criteriaBuilder) ->
                Boolean.TRUE.equals(availableOnly)
                        ? criteriaBuilder.isNull(root.get("patient"))
                        : null;
    }

    public static Specification<Visit> startFrom(LocalDateTime from) {
        return (root, query, criteriaBuilder) ->
                from == null
                        ? null
                        : criteriaBuilder.greaterThanOrEqualTo(root.get("startDateTime"), from);
    }

    public static Specification<Visit> startTo(LocalDateTime to) {
        return (root, query, criteriaBuilder) ->
                to == null
                        ? null
                        : criteriaBuilder.lessThanOrEqualTo(root.get("startDateTime"), to);
    }

    public static Specification<Visit> onDay(LocalDate day) {
        if (day == null) return null;
        LocalDateTime from = day.atStartOfDay();
        LocalDateTime to = day.plusDays(1).atStartOfDay().minusNanos(1);
        return Specification.allOf(startFrom(from), startTo(to));
    }

    public static Specification<Visit> excludePast(boolean includePast) {
        return (root, query, criteriaBuilder) ->
                includePast
                        ? null
                        : criteriaBuilder.greaterThanOrEqualTo(root.get("startDateTime"), LocalDateTime.now());
    }
}
