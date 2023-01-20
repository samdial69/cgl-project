package com.example.cglproject.services.comission;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.models.Commission;
import com.example.cglproject.repositories.CommissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommissionServiceImplTest {

    private Commission commission;

    private CommissionServiceImpl service;

    @Mock
    private CommissionRepository repository;


    @BeforeEach
    void setUp() {
        service = new CommissionServiceImpl(repository);

        this.commission = Commission.builder()
                .id(1L)
                .commission(10000)
                .business(Business.builder().build())
                .recipient(BusinessProvider.builder().build())
                .build();
    }

    @Test
    void getAllCommissions() {
        //when
        service.getAllCommissions();

        //then
        verify(repository).findAll();
    }

    @Test
    void findById() {
        //when
        service.findById(commission.getId());

        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(repository).findById(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue(), commission.getId());
    }

    @Test
    void create() {
        //when
        service.create(commission);

        //then
        ArgumentCaptor<Commission> argumentCaptor = ArgumentCaptor.forClass(Commission.class);
        verify(repository).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue(), commission);
    }

    @Test
    void update() {
        //given
        Commission commissionUpdated = Commission.builder()
                .id(1L)
                .commission(20000)
                .business(Business.builder().build())
                .recipient(BusinessProvider.builder().build())
                .build();
        given(service.findById(any(Long.class))).willReturn(Optional.of(commission));

        //when
        service.update(commission.getId(), commissionUpdated);

        //then
        ArgumentCaptor<Commission> argumentCaptor = ArgumentCaptor.forClass(Commission.class);
        verify(repository).save(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue().getId(), commissionUpdated.getId());
        assertEquals(argumentCaptor.getValue(), commissionUpdated);
    }

    @Test
    void givenWrongId_whenUpdateCommission_thenReturnNull() {
        //given
        Long id = 2L;

        //when
        Commission commissionUpdated = service.update(id, commission);

        //then
        assertNull(commissionUpdated);
        verify(repository, never()).save(any());
    }

    @Test
    void delete() {
        //given
        given(service.findById(any(Long.class))).willReturn(Optional.of(commission));

        //when

        boolean isDeleted = service.delete(commission.getId());

        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(repository).deleteById(argumentCaptor.capture());

        assertTrue(isDeleted);
        assertEquals(argumentCaptor.getValue(), commission.getId());
    }

    @Test
    void givenWrongId_whenDeleteCommission_thenReturnFalse() {
        //given
        Long id = 2L;

        //when
        boolean deleted = service.delete(id);

        //then
        assertFalse(deleted);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void deleteComission(){
        //when
        boolean deleted = service.delete(commission);

        //then
        ArgumentCaptor<Commission> argumentCaptor = ArgumentCaptor.forClass(Commission.class);
        verify(repository).delete(argumentCaptor.capture());

        assertTrue(deleted);
        assertThat(argumentCaptor.getValue()).isEqualTo(commission);
    }
}
