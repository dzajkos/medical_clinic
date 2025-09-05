package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.exception.VisitDateTimeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VisitValidator {


    public static void validateStart(LocalDateTime start) {
        if (start.isBefore(LocalDateTime.now())) {
            throw new VisitDateTimeException("Visit can't be set in the past", HttpStatus.CONFLICT);
        }
    }

    public static void validateEnd(LocalDateTime start, LocalDateTime end) {
        if (!end.isAfter(start)) {
            throw new VisitDateTimeException("Visit end time must be after start time", HttpStatus.CONFLICT);
        }
    }

    public static void validateCorrectIncrements(LocalDateTime start, LocalDateTime end) {
        if (start.getMinute() % 15 != 0 || end.getMinute() % 15 !=0) {
            throw new VisitDateTimeException("Visit time can only be set in equal 15 minute intervals", HttpStatus.CONFLICT);
        }
    }

    public static void validateNewVisit(LocalDateTime start, LocalDateTime end) {
        validateStart(start);
        validateEnd(start, end);
        validateCorrectIncrements(start, end);
    }
}

