package com.dzajkos.medical_clinic.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PatientSelector {
    private List<Long> patientIDs;
}
