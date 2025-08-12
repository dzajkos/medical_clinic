package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.mapper.PatientMapper;
import com.dzajkos.medical_clinic.model.CreatePatientCommand;
import com.dzajkos.medical_clinic.model.PageDTO;
import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.model.PatientDTO;
import com.dzajkos.medical_clinic.service.PatientSelector;
import com.dzajkos.medical_clinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @Tag(name = "find")
    @Operation(summary = "Get list of all patients")
    @ApiResponse(responseCode = "200", description = "Got list of patients (even if empty)")
    @GetMapping
    public PageDTO<Patient> getPatients(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Pageable pageAndSize = PageRequest.of(page, size);
        Page<Patient> patientPage = patientService.getPatients(pageAndSize);
        return new PageDTO<>(patientPage);
    }

    @Tag(name = "find")
    @Operation(summary = "Get Patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Patient",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient with given email not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @GetMapping("/{email}")
    public PatientDTO getPatientByEmail(@PathVariable("email") String email) {
        return patientMapper.patientToDTO(patientService.getPatient(email));
    }

    @Tag(name = "create")
    @Operation(summary = "Create new Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Patient already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public PatientDTO addPatient(@RequestBody CreatePatientCommand createPatientCommand) {
        return patientMapper.patientToDTO(patientService.addPatient(patientMapper.createCommandToPatient(createPatientCommand)));
    }

    @Tag(name = "update")
    @Operation(summary = "Replaces Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient replaced",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "409",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PutMapping("/{email}")
    public PatientDTO updatePatient(@PathVariable String email, @RequestBody CreatePatientCommand updatedPatientDTO) {
        return patientMapper.patientToDTO(patientService.updatePatient(email, patientMapper.createCommandToPatient(updatedPatientDTO)));
    }

    @Tag(name = "delete")
    @Operation(summary = "Deletes Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient replaced"),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(NO_CONTENT)
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @Tag(name = "update")
    @Operation(summary = "Updates Patient password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "password updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "409",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PatchMapping("{email}")
    public PatientDTO changePassword(@PathVariable String email, @RequestBody CreatePatientCommand patientWithChangedPasswordDTO) {
        return patientMapper.patientToDTO(patientService.changePassword(email, patientWithChangedPasswordDTO.password()));
    }

    @DeleteMapping
    public void batchDeletePatients(@RequestBody PatientSelector patientSelector) {
        patientService.batchDeletePatients(patientSelector);
    }
}
