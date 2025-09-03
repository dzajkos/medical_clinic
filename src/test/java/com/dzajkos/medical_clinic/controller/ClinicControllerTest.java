package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.model.Clinic;
import com.dzajkos.medical_clinic.model.ClinicDTO;
import com.dzajkos.medical_clinic.model.PageDTO;
import com.dzajkos.medical_clinic.service.ClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClinicControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    ClinicService clinicService;

    @Test
    void getClinics_ReturnsPageDTOofClinicDTO() throws Exception {
        PageDTO<ClinicDTO> page = PageDTO.<ClinicDTO>builder()
                .content(List.of(ClinicDTO.builder()
                                .id(1L)
                                .name("clinicName1")
                                .city("clinicCity")
                                .build(),
                        ClinicDTO.builder()
                                .id(2L)
                                .name("clinicName2")
                                .city("clinicCity")
                                .build()))
                .totalPages(1)
                .totalElements(2)
                .build();
        when(clinicService.getClinics(any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/clinics"))
                .andExpectAll(
                        jsonPath("$.totalPages").value(1),
                        jsonPath("$.content[0].id").value(1L),
                        jsonPath("$.content[1].id").value(2L),
                        jsonPath("$.content[0].name").value("clinicName1")
                );
    }

    @Test
    void getClinicByName_ReturnsClinicDTO() throws Exception {
        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("clinicName1")
                .city("clinicCity")
                .build();
        when(clinicService.getClinic(anyString())).thenReturn(clinic);

        mockMvc.perform(MockMvcRequestBuilders.get("/clinics/clinicName1"))
                .andExpectAll(
                        jsonPath("$.id").value(1L),
                        jsonPath("$.name").value("clinicName1"),
                        jsonPath("$.city").value("clinicCity")
                );
    }

    @Test
    void addClinic_AddsClinic() throws Exception {
        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("clinicName1")
                .city("clinicCity")
                .build();
        when(clinicService.addClinic(any())).thenReturn(clinic);

        mockMvc.perform(MockMvcRequestBuilders.post("/clinics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clinic))
                )
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.name").value("clinicName1"),
                        jsonPath("$.city").value("clinicCity")
                );
    }

    @Test
    void deleteClinic_DeletesClinic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/clinics/name"))
                .andExpect(status().isNoContent());
    }
}
