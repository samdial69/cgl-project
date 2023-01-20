package com.example.cglproject.services.parameter;

import com.example.cglproject.models.Parameter;
import com.example.cglproject.repositories.ParameterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParameterServiceImplTest {

    private Parameter parameter;
    private ParameterServiceImpl service;

    @Mock
    private ParameterRepository repository;

    @BeforeEach
    void setUp() {
        service = new ParameterServiceImpl(repository);
        this.parameter = Parameter.builder()
                .id(1L)
                .levelOfSponsorship(5)
                .percentageOfInitialCommission(0.2)
                .numberOfBusinessToBeAffiliated(2)
                .numberOfMonthsAffiliation(2)
                .build();
    }

    @Test
    void findById() {
        //when
        service.findById(parameter.getId());
        //then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(repository).findById(captor.capture());
        assertThat(captor.getValue()).isEqualTo(parameter.getId());
    }

    @Test
    void canGetAllParameter() {
        //when
        service.findAll();

        //then
        verify(repository).findAll();
    }

    @Test
    void save() {
        //when
        service.save(parameter);

        //then
        ArgumentCaptor<Parameter> parameterArgumentCaptor = ArgumentCaptor.forClass(Parameter.class);
        verify(repository).save(parameterArgumentCaptor.capture());

        assertThat(parameterArgumentCaptor.getValue()).isEqualTo(parameter);

    }

    @Test
    @DisplayName("Update parameter with good id")
    void update() {
        //given
        Parameter parameterToUpdate = Parameter.builder()
                .levelOfSponsorship(3)
                .percentageOfInitialCommission(0.2)
                .numberOfBusinessToBeAffiliated(2)
                .numberOfMonthsAffiliation(2)
                .build();
        given(repository.findById(any())).willReturn(Optional.of(parameter));

        //when
        service.update(parameter.getId(), parameterToUpdate);

        //then
        ArgumentCaptor<Parameter> parameterArgumentCaptor = ArgumentCaptor.forClass(Parameter.class);
        verify(repository).save(parameterArgumentCaptor.capture());

        assertThat(parameterArgumentCaptor.getValue().getId()).isEqualTo(parameterToUpdate.getId());
        assertThat(parameterArgumentCaptor.getValue()).isEqualTo(parameterToUpdate);
    }

    @Test
    @DisplayName("Update parameter with wrong id")
    void updateWithWrongId() {
        //given
        Long id = 2L;
        //when
        Parameter updatedParameter = service.update(id, parameter);

        //then
        assertNull(updatedParameter);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Delete parameter with good id")
    void delete() {
        //given
        given(repository.findById(any())).willReturn(Optional.of(parameter));

        //when
        boolean isDeleted = service.delete(parameter.getId());

        //then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(repository).deleteById(captor.capture());

        assertTrue(isDeleted);
        assertThat(captor.getValue()).isEqualTo(parameter.getId());
    }

    @Test
    @DisplayName("Delete parameter with wrong id")
    void deleteWithWrongId() {
        //given
        Long id = 2L;
        //when
        boolean isDeleted = service.delete(id);

        //then
        assertFalse(isDeleted);
        verify(repository, never()).deleteById(any());
    }
}
