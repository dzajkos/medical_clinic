package com.dzajkos.medical_clinic.controller;

import com.dzajkos.medical_clinic.model.PageDTO;
import com.dzajkos.medical_clinic.model.Patient;
import com.dzajkos.medical_clinic.model.PatientDTO;
import com.dzajkos.medical_clinic.service.PatientService;
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
public class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    PatientService patientService;

    @Test
    void getPatients_ReturnsPageDTOofPatientDTO() throws Exception {
        PageDTO<PatientDTO> page = PageDTO.<PatientDTO>builder()
                .content(List.of(PatientDTO.builder()
                                .id(1L)
                                .email("kowalski1@example.com")
                                .firstName("Jan")
                                .build(),
                        PatientDTO.builder()
                                .id(2L)
                                .email("kowalski2@example.com")
                                .firstName("Jan")
                                .build()))
                .totalPages(1)
                .totalElements(2)
                .build();
        when(patientService.getPatients(any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/patients"))
                .andExpectAll(
                        jsonPath("$.totalPages").value(1),
                        jsonPath("$.content[0].id").value(1L),
                        jsonPath("$.content[1].id").value(2L),
                        jsonPath("$.content[0].email").value("kowalski1@example.com")
                );
    }

    @Test
    void getPatientByEmail_ReturnsPatientDTO() throws Exception {
        Patient patient = Patient.builder()
                .id(1L)
                .email("kowalski1@example.com")
                .firstName("Jan")
                .build();
        when(patientService.getPatient(anyString())).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/kowalski1@example.com"))
                .andExpectAll(
                        jsonPath("$.id").value(1L),
                        jsonPath("$.email").value("kowalski1@example.com"),
                        jsonPath("$.firstName").value("Jan")
                );
    }

    @Test
    void addPatient_AddsPatient() throws Exception {
        Patient patient = Patient.builder()
                .id(1L)
                .email("kowalski1@example.com")
                .firstName("Jan")
                .build();
        when(patientService.addPatient(any())).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.email").value("kowalski1@example.com"),
                        jsonPath("$.firstName").value("Jan"));
    }

    @Test
    void updatePatient_ReplacesPatient() throws Exception {
        Patient updatedPatient = Patient.builder()
                .id(1L)
                .email("kowalski1@example.com")
                .firstName("JanZmieniony")
                .build();
        when(patientService.updatePatient(any(), any())).thenReturn(updatedPatient);

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/kowalski1@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.firstName").value("JanZmieniony"));
    }

    @Test
    void deletePatient_DeletesPatient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/email"))
                .andExpect(status().isNoContent());
    }

    @Test
    void changePassword_UpdatesPatientPassword() throws Exception {
        Patient patientWithChangedPassword = Patient.builder()
                .id(1L)
                .email("kowalski1@example.com")
                .firstName("Jan")
                .password("changedPassword")
                .build();
        when(patientService.changePassword(any(), any())).thenReturn(patientWithChangedPassword);

        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/kowalski1@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientWithChangedPassword)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1L));
    }

    @Test
    void batchDeletePatients_DeletesPatientsByIdList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/email"))
                .andExpect(status().isNoContent());
    }
}
