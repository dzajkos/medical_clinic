package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.mapper.ClinicMapper;
import com.dzajkos.medical_clinic.mapper.DoctorMapper;
import com.dzajkos.medical_clinic.model.*;
import com.dzajkos.medical_clinic.service.ClinicSelector;
import com.dzajkos.medical_clinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final ClinicMapper clinicMapper;

    @Tag(name = "find")
    @Operation(summary = "Get list of all doctors")
    @ApiResponse(responseCode = "200", description = "Got list of doctors (even if empty)")
    @GetMapping
    public PageDTO<DoctorDTO> getDoctors(Pageable pageable) {
        return doctorService.getDoctors(pageable);
    }

    @Tag(name = "find")
    @Operation(summary = "Get Doctor by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Doctor",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor with given email not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @GetMapping("/{email}")
    public DoctorDTO getDoctorByEmail(@PathVariable("email") String email) {
        return doctorMapper.doctorToDTO(doctorService.getDoctor(email));
    }

    @Tag(name = "create")
    @Operation(summary = "Create new Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Doctor already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public DoctorDTO addDoctor(@RequestBody CreateDoctorCommand createDoctorCommand) {
        return doctorMapper.doctorToDTO(doctorService.addDoctor(doctorMapper.createCommandToDoctor(createDoctorCommand)));
    }

    @Tag(name = "delete")
    @Operation(summary = "deletes Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor replaced"),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(NO_CONTENT)
    public void deleteDoctor(@PathVariable String email) {
        doctorService.deleteDoctor(email);
    }

    @Tag(name = "assign")
    @Operation(summary = "assigns Clinic to Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "404", description = "Clinic not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PatchMapping("/{email}")
    public ClinicDTO assignClinic(@PathVariable String email, @RequestBody ClinicSelector clinicSelector) {
        return clinicMapper.clinicToDTO(doctorService.assignClinic(email, clinicSelector.getName()));
    }
}
