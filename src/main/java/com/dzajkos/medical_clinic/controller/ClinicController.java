package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.mapper.ClinicMapper;
import com.dzajkos.medical_clinic.model.CreateClinicCommand;
import com.dzajkos.medical_clinic.model.ClinicDTO;
import com.dzajkos.medical_clinic.service.ClinicService;
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
@RequestMapping("/clinics")
public class ClinicController {
    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;

    @Tag(name = "find")
    @Operation(summary = "Get list of all clinics")
    @ApiResponse(responseCode = "200", description = "Got list of clinics (even if empty)")
    @GetMapping
    public List<ClinicDTO> getClinics(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Pageable pageAndSize = PageRequest.of(page, size);
        return clinicService.getClinics(pageAndSize).stream()
                .map(clinicMapper::clinicToDTO)
                .toList();
    }

    @Tag(name = "find")
    @Operation(summary = "Get Clinic by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Clinic",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Clinic with given name not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @GetMapping("/{name}")
    public ClinicDTO getClinicByEmail(@PathVariable("name") String name) {
        return clinicMapper.clinicToDTO(clinicService.getClinic(name));
    }

    @Tag(name = "create")
    @Operation(summary = "Create new Clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clinic created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Clinic already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public ClinicDTO addClinic(@RequestBody CreateClinicCommand createClinicCommand) {
        return clinicMapper.clinicToDTO(clinicService.addClinic(clinicMapper.createCommandToClinic(createClinicCommand)));
    }

    @Tag(name = "delete")
    @Operation(summary = "deletes Clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Clinic replaced"),
            @ApiResponse(responseCode = "404", description = "Clinic not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @DeleteMapping("/{name}")
    @ResponseStatus(NO_CONTENT)
    public void deleteClinic(@PathVariable String name) {
        clinicService.deleteClinic(name);
    }
}
