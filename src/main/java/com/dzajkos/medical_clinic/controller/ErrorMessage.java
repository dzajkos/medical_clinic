package com.dzajkos.medical_clinic.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
public class ErrorMessage {

    public String message;
    HttpStatus httpStatus;
    OffsetDateTime date;
}
