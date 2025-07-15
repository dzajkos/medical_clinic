package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable("email") String email) {
        return patientService.getPatient(email);
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @PutMapping("/{email}")
    public Patient updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
        return patientService.updatePatient(email, updatedPatient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(NO_CONTENT)
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @PatchMapping("{email}")
    public Patient changePassword(@PathVariable String email, @RequestBody Patient patientWithChangedPassword) {
        return patientService.changePassword(email, patientWithChangedPassword.getPassword());
    }
}
