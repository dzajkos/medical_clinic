package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.model.Clinic;
import com.dzajkos.medical_clinic.model.Doctor;
import com.dzajkos.medical_clinic.model.DoctorDTO;
import com.dzajkos.medical_clinic.model.PageDTO;
import com.dzajkos.medical_clinic.service.ClinicSelector;
import com.dzajkos.medical_clinic.service.DoctorService;
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
public class DoctorControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    DoctorService doctorService;

    @Test
    void getDoctors_ReturnsPageDTOofDoctorDTO() throws Exception {
        PageDTO<DoctorDTO> page = PageDTO.<DoctorDTO>builder()
                .content(List.of(DoctorDTO.builder()
                                .id(1L)
                                .email("kowalski1@example.com")
                                .firstName("Jan")
                                .build(),
                        DoctorDTO.builder()
                                .id(2L)
                                .email("kowalski2@example.com")
                                .firstName("Jan")
                                .build()))
                .totalPages(1)
                .totalElements(2)
                .build();
        when(doctorService.getDoctors(any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors"))
                .andExpectAll(
                        jsonPath("$.totalPages").value(1),
                        jsonPath("$.content[0].id").value(1L),
                        jsonPath("$.content[1].id").value(2L),
                        jsonPath("$.content[0].email").value("kowalski1@example.com")
                );
    }

    @Test
    void getDoctorByEmail_ReturnsDoctorDTO() throws Exception {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .email("kowalski1@example.com")
                .firstName("Jan")
                .build();
        when(doctorService.getDoctor(anyString())).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/kowalski1@example.com"))
                .andExpectAll(
                        jsonPath("$.id").value(1L),
                        jsonPath("$.email").value("kowalski1@example.com"),
                        jsonPath("$.firstName").value("Jan")
                );
    }

    @Test
    void addDoctor_AddsDoctor() throws Exception {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .email("kowalski1@example.com")
                .firstName("Jan")
                .build();
        when(doctorService.addDoctor(any())).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctor)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.email").value("kowalski1@example.com"),
                        jsonPath("$.firstName").value("Jan"));
    }

    @Test
    void deleteDoctor_DeletesDoctor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/email"))
                .andExpect(status().isNoContent());
    }

    @Test
    void assignClinic_AssignsAndReturnsClinic() throws Exception {
        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("clinicName")
                .city("clinicCity")
                .build();
        ClinicSelector clinicSelector = new ClinicSelector("clinicName");
        when(doctorService.assignClinic(any(), any())).thenReturn(clinic);

        mockMvc.perform(MockMvcRequestBuilders.patch("/doctors/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clinicSelector)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.name").value("clinicName"),
                        jsonPath("$.city").value("clinicCity"));
    }
}
