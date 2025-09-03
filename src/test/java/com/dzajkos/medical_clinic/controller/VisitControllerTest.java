package com.dzajkos.medical_clinic.controller;
import com.dzajkos.medical_clinic.model.CreateVisitCommand;
import com.dzajkos.medical_clinic.model.Doctor;
import com.dzajkos.medical_clinic.model.Visit;
import com.dzajkos.medical_clinic.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    VisitService visitService;

    @Test
    void addVisit_AddsVisit() throws Exception {
        Visit visit = Visit.builder()
                .id(1L)
                .startDateTime(LocalDateTime.now().withMinute(0).plusHours(2))
                .endDateTime(LocalDateTime.now().withMinute(0).plusHours(3))
                .doctor(Doctor.builder()
                        .id(1L)
                        .email("kowalski1@example.com")
                        .firstName("Jan")
                        .build())
                .build();
        when(visitService.addVisit(any())).thenReturn(visit);

        mockMvc.perform(MockMvcRequestBuilders.post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visit))
                )
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.startDateTime").value(visit.getStartDateTime().toString()),
                        jsonPath("$.endDateTime").value(visit.getEndDateTime().toString())
                );
    }
}
