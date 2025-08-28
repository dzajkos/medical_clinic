package com.dzajkos.medical_clinic.service;

import com.dzajkos.medical_clinic.mapper.ClinicMapper;
import com.dzajkos.medical_clinic.model.Clinic;
import com.dzajkos.medical_clinic.model.ClinicDTO;
import com.dzajkos.medical_clinic.model.PageDTO;
import com.dzajkos.medical_clinic.repository.ClinicRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ClinicServiceTest {

    ClinicService clinicService;
    ClinicRepository clinicRepository;
    ClinicMapper clinicMapper;

    @BeforeEach
    void StartUp() {
        this.clinicRepository = Mockito.mock(ClinicRepository.class);
        this.clinicMapper = Mappers.getMapper(ClinicMapper.class);
        this.clinicService = new ClinicService(clinicRepository, clinicMapper);
    }

    @Test
    void getClinics_WhenGivenPageable_ShouldReturnPageDTOofClinicDTO() {
        // given
        Pageable pageable = PageRequest.of(0, 2);
        List<Clinic> clinicList = List.of(
                Clinic.builder()
                        .id(1L)
                        .name("clinicName1")
                        .city("clinicCity")
                        .postalCode("01-001")
                        .street("clinicStreet")
                        .buildingNo("10/15")
                        .build(),
                Clinic.builder()
                        .id(2L)
                        .name("clinicName2")
                        .city("clinicCity")
                        .postalCode("01-001")
                        .street("clinicStreet")
                        .buildingNo("10/15")
                        .build(),
                Clinic.builder()
                        .id(3L)
                        .name("clinicName3")
                        .city("clinicCity")
                        .postalCode("01-001")
                        .street("clinicStreet")
                        .buildingNo("10/15")
                        .build(),
                Clinic.builder()
                        .id(4L)
                        .name("clinicName4")
                        .city("clinicCity")
                        .postalCode("01-001")
                        .street("clinicStreet")
                        .buildingNo("10/15")
                        .build()
        );
        Page<Clinic> clinicPage = new PageImpl<Clinic>(clinicList);
        when(clinicRepository.findAll(pageable)).thenReturn(clinicPage);

        // when
        PageDTO<ClinicDTO> result = clinicService.getClinics(pageable);

        // then
        Assertions.assertAll(
                () -> Assertions.assertEquals("clinicName4", result.getContent().getLast().name()),
                () -> Assertions.assertEquals(4, result.getTotalElements())
        );
    }

    @Test
    void getClinic_WhenGivenCorrectName_ShouldReturnClinic() {

        // given
        String name = "clinicName1";
        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("clinicName1")
                .city("clinicCity")
                .postalCode("01-001")
                .street("clinicStreet")
                .buildingNo("10/15")
                .build();
        when(clinicRepository.findByName(name)).thenReturn(Optional.of(clinic));

        // when
        Clinic result = clinicService.getClinic(name);

        // then
        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("clinicStreet", result.getStreet())
        );
    }

    @Test
    void addClinic_WhenGivenClinicHasUniqueName_shouldAddAndReturnClinic() {

        // given
        Clinic clinic = Clinic.builder()
                .name("clinicName1")
                .city("clinicCity")
                .postalCode("01-001")
                .street("clinicStreet")
                .buildingNo("10/15")
                .build();
        when(clinicRepository.findByName(clinic.getName())).thenReturn(Optional.empty());
        when(clinicRepository.save(any(Clinic.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        Clinic result = clinicService.addClinic(clinic);

        // then
        assertAll(
                () -> assertEquals("clinicName1", result.getName()),
                () -> assertEquals("clinicStreet", result.getStreet())
        );
    }
}
