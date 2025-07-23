package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.mapper.PatientMapper;
import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.model.PatientCreationDTO;
import com.dzajkos.medical_clinic.model.PatientResponseDTO;
import com.dzajkos.medical_clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<PatientResponseDTO> getPatients() {
        return patientService.getPatients().stream()
                .map(PatientMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{email}")
    public PatientResponseDTO getPatientByEmail(@PathVariable("email") String email) {
        return PatientMapper.toResponseDTO(patientService.getPatient(email));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public PatientResponseDTO addPatient(@RequestBody PatientCreationDTO patientDTO) {
        return PatientMapper.toResponseDTO(patientService.addPatient(PatientMapper.toEntity(patientDTO)));
    }

    @PutMapping("/{email}")
    public PatientResponseDTO updatePatient(@PathVariable String email, @RequestBody PatientCreationDTO updatedPatientDTO) {
        return PatientMapper.toResponseDTO(patientService.updatePatient(email, PatientMapper.toEntity(updatedPatientDTO)));
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(NO_CONTENT)
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @PatchMapping("{email}")
    public PatientResponseDTO changePassword(@PathVariable String email, @RequestBody PatientCreationDTO patientWithChangedPasswordDTO) {
        return PatientMapper.toResponseDTO(patientService.changePassword(email, patientWithChangedPasswordDTO.getPassword()));
    }
}
