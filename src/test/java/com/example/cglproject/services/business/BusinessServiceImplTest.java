package com.example.cglproject.services.business;

import com.example.cglproject.models.Business;
import com.example.cglproject.repositories.BusinessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BusinessServiceImplTest {
    private Business business;

    private BusinessServiceImpl service;

    @Mock
    private BusinessRepository repository;

    @BeforeEach
    void setUp() {
        service = new BusinessServiceImpl(repository);
        this.business = Business.builder()
                .id(1L)
                .title("Title")
                .createdAt(new Date())
                .build();
    }

    @Test
    void getAllBusinesses() {
        //when
        service.getAllBusinesses();

        //then
        verify(repository).findAll();
    }

    @Test
    void findById() {
        //when
        service.findById(business.getId());

        //then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(repository).findById(captor.capture());
        assertEquals(captor.getValue(), business.getId());
    }

    @Test
    void create() {
        //when
        service.create(business);

        //then
        ArgumentCaptor<Business> captor = ArgumentCaptor.forClass(Business.class);
        verify(repository).save(captor.capture());
        assertEquals(captor.getValue(), business);
    }

    @Test
    @DisplayName("Update business with good id")
    void update() {
        //given
        Business businessToUpdate = Business.builder()
                .title("Title Updated")
                .createdAt(new Date())
                .build();
        given(repository.findById(any(Long.class ))).willReturn(Optional.of(business));
        //when
        service.update(business.getId(), businessToUpdate);

        //then
        ArgumentCaptor<Business> captor = ArgumentCaptor.forClass(Business.class);
        verify(repository).save(captor.capture());
        assertEquals(captor.getValue().getId(), business.getId());
        assertEquals(captor.getValue(), businessToUpdate);
    }

    @Test
    @DisplayName("Update business with wrong id")
    void updateWithWrongId() {
        //given
        Long id = 2L;
        //when
        Business updatedBusiness = service.update(id, business);

        //then
        assertNull(updatedBusiness);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Delete business with good id")
    void delete() {
        //given
        given(repository.findById(any(Long.class ))).willReturn(Optional.of(business));

        //when
        boolean isDeleted = service.delete(business.getId());

        //then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(repository).deleteById(captor.capture());

        assertTrue(isDeleted);
        assertEquals(captor.getValue(), business.getId());
    }

    @Test
    @DisplayName("Delete business with wrong id")
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
