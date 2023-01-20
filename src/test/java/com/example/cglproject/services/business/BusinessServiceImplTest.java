package com.example.cglproject.services.business;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.Commission;
import com.example.cglproject.repositories.BusinessProviderRepository;
import com.example.cglproject.repositories.BusinessRepository;
import com.example.cglproject.repositories.CommissionRepository;
import com.example.cglproject.repositories.ParameterRepository;
import com.example.cglproject.services.business_provider.IBusinesProviderService;
import com.example.cglproject.services.comission.CommissionServiceImpl;
import com.example.cglproject.services.comission.ICommissionService;
import com.example.cglproject.services.parameter.IParameterService;
import com.example.cglproject.services.parameter.ParameterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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

    @Mock
    private BusinessProviderRepository providerRepository;

    @Mock
    private ICommissionService commissionService;

    @Mock
    private IParameterService parameterService;

    @BeforeEach
    void setUp() {
        service = new BusinessServiceImpl(repository, commissionService, providerRepository, parameterService);

        this.business = Business.builder()
                .id(1L)
                .title("Title")
                .commissions(List.of(Commission.builder().id(1L).build()))
                .createdAt(LocalDate.now())
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
                .createdAt(LocalDate.now())
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
    @Disabled
    void delete() {
        //given
        given(repository.findById(any(Long.class ))).willReturn(Optional.of(business));

        //when
        boolean isDeleted = service.delete(business.getId());

        //then
        ArgumentCaptor<Business> captor = ArgumentCaptor.forClass(Business.class);
        verify(service).delete(captor.capture());

        assertTrue(isDeleted);
        assertEquals(captor.getValue(), business);
    }

    @Test
    @DisplayName("Delete business and commissions included with business id")
    void deleteWithCommissions() {

        //given
        given(repository.findById(any(Long.class ))).willReturn(Optional.of(business));

        //when
        boolean isDeleted = service.delete(business.getId());
        List<Commission> commisions = business.getCommissions();

        //then
        ArgumentCaptor<List<Commission>> captor = ArgumentCaptor.forClass(List.class);
        verify(commissionService).delete(captor.capture());

        ArgumentCaptor<Business> captorBusiness = ArgumentCaptor.forClass(Business.class);
        verify(repository).delete(captorBusiness.capture());

        assertTrue(isDeleted);
        assertEquals(captor.getAllValues().size(), commisions.size());
        assertEquals(captorBusiness.getValue(), business);
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
