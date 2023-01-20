package com.example.cglproject.services.business_provider;

import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.repositories.BusinessProviderRepository;
import com.example.cglproject.services.business.IBusinessService;
import com.example.cglproject.services.comission.ICommissionService;
import com.example.cglproject.services.parameter.IParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BusinessProviderServiceImplTest {

    private BusinessProviderServiceImpl service;

    private BusinessProvider businessProvider;

    @Mock
    private BusinessProviderRepository repository;

    @Mock
    private IBusinessService businessService;

    @Mock
    private ICommissionService commissionService;

    @Mock
    private IParameterService parameterService;

    @BeforeEach
    void setUp() {
        service = new BusinessProviderServiceImpl(repository, businessService, commissionService, parameterService);
        this.businessProvider = BusinessProvider.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .build();
    }

    @Test
    @DisplayName("Test getAllBusinessProviders")
    void getAllBusinessProviders() {
        //when
        service.getAllBusinessProviders();

        //then
        verify(repository).findAll();

    }

    @Test
    @DisplayName("Test findById")
    void getById() {
        //when
        service.getById(businessProvider.getId());

        //then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(repository).findById(captor.capture());
        assertEquals(captor.getValue(), businessProvider.getId());
    }

    @Test
    void create() {
        //when
        service.create(businessProvider);

        //then
        ArgumentCaptor<BusinessProvider> captor = ArgumentCaptor.forClass(BusinessProvider.class);
        verify(repository).save(captor.capture());

        assertEquals(captor.getValue(), businessProvider);
    }

    @Test
    @DisplayName("Update businessProvider with good id")
    void update() {
        //given
        BusinessProvider businessProviderToUpdate = BusinessProvider.builder()
                .firstname("Jane")
                .lastname("Doe")
                .build();
        given(repository.findById(businessProvider.getId())).willReturn(Optional.of(businessProvider));

        //when
        service.update(businessProvider.getId(), businessProviderToUpdate);

        //then
        ArgumentCaptor<BusinessProvider> captor = ArgumentCaptor.forClass(BusinessProvider.class);
        verify(repository).save(captor.capture());

        assertEquals(captor.getValue().getId(), businessProviderToUpdate.getId());
        assertEquals(captor.getValue(), businessProviderToUpdate);
    }

    @Test
    @DisplayName("Update businessProvider with wrong id")
    void updateWithWrongId() {
        //given
        Long id = 2L;

        //when
        BusinessProvider updatedBusinessProvider =  service.update(id, businessProvider);

        //then
        assertNull(updatedBusinessProvider);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Delete businessProvider with good id")
    void delete() {
        //given
        given(repository.findById(any(Long.class))).willReturn(Optional.of(businessProvider));

        //when
        boolean isDeleted = service.delete(businessProvider.getId());

        //then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(repository).deleteById(captor.capture());

        assertTrue(isDeleted);
        assertEquals(captor.getValue(), businessProvider.getId());
    }

    @Test
    @DisplayName("Delete businessProvider with wrong id")
    void deleteWithWrongId() {
        //given
       Long id = 2L;

        //when
        boolean isDeleted = service.delete(id);

        //then
        assertFalse(isDeleted);
        verify(repository, never()).deleteById(any(Long.class));
    }
}
