package com.example.cglproject.resources.business_provider;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.models.Commission;
import com.example.cglproject.models.Parameter;
import com.example.cglproject.services.business_provider.BusinessProviderServiceImpl;
import com.example.cglproject.services.parameter.IParameterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusinessProviderResource.class)
@ContextConfiguration(classes = {BusinessProviderResource.class})
class BusinessProviderResourceTest {
    private BusinessProvider provider;
    private Business business;
    private Commission commission;
    private Parameter parameter;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessProviderServiceImpl service;

    @MockBean
    private IParameterService parameterService;

    @BeforeEach
    void setUp() {
        BusinessProvider defaultProvider = BusinessProvider.builder()
                .id(2L)
                .firstname("John")
                .lastname("Doe")
                .build();

        Commission defaultCommission = Commission.builder()
                .id(1L)
                .commission(1000)
                .recipient(defaultProvider)
                .build();



        this.parameter = Parameter.builder()
                .id(1L)
                .levelOfSponsorship(5)
                .percentageOfInitialCommission(5)
                .percentageOfNextCommission(50)
                .numberOfBusinessToBeAffiliated(1)
                .numberOfMonthsAffiliation(3)
                .build();

        this.business = Business.builder()
                .id(1L)
                .title("title")
                .createdAt(LocalDate.now())
                .commissions(List.of(defaultCommission))
                .provider(defaultProvider)
                .build();

        this.commission = Commission.builder()
                .id(1L)
                .commission(1000)
                .business(this.business)
                .recipient(this.provider)
                .build();

        this.provider = BusinessProvider.builder()
                .id(1L)
                .lastname("Doe")
                .firstname("John")
                .sponsor(this.provider)
                .businesses(List.of(business))
                .commissions(List.of(commission))
//                .sponsored(List.of(this.provider))
                .build();



    }

    @Test
    @DisplayName("When GET /business_provider is not empty then return list of business providers")
    void whenGetBusinessProviderIsNotEmpty_thenReturnListOfBusinessProviders() throws Exception {
        //given
        List<BusinessProvider> businessProviders = List.of(provider);
        //when
        given(service.getAllBusinessProviders()).willReturn(businessProviders);
        given(parameterService.getApplicationParameters()).willReturn(parameter);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/"))
                .andDo(print())
                .andExpect(view().name("businessProvidersPages/apporteur-daffaires"))
                .andExpect(model().attributeExists("providers"))
                .andExpect(model().attribute("providers", businessProviders))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("John");

    }

    @Test
    @DisplayName("When GET /business_provider is empty then return empty list of business providers")
    void whenGetBusinessProviderIsEmpty_thenReturnEmptyListOfBusinessProviders() throws Exception {
        //when
        given(service.getAllBusinessProviders()).willReturn(List.of());
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/"))
                .andDo(print())
                .andExpect(view().name("businessProvidersPages/apporteur-daffaires"))
                .andExpect(model().attributeExists("providers"))
                .andExpect(model().attribute("providers",List.of()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("When GET /business_provider/{id} is not empty then return business provider")
    void whenGetBusinessProviderByIdIsNotEmpty_thenReturnBusinessProvider() throws Exception {
        //when
        given(service.getById(any(Long.class))).willReturn(Optional.of(provider));
        given(parameterService.getApplicationParameters()).willReturn(parameter);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("businessProvidersPages/apporteur-daffaires-liste-de-tous-les-affaires"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attribute("provider", provider))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("John");
    }

    @Test
    @DisplayName("When GET /business_provider/{id} is empty then return empty business provider")
    void whenGetBusinessProviderByIdIsEmpty_thenReturnEmptyBusinessProvider() throws Exception {
        //when
        given(service.getById(any(Long.class))).willReturn(Optional.empty());
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("error/error404"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attribute("provider", Optional.empty()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("When GET /business_provider/create then return business provider form")
    void whenGetBusinessProviderCreate_thenReturnBusinessProviderForm() throws Exception {
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/create"))
                .andDo(print())
                .andExpect(view().name("businessProvidersPages/add-business-providers"))
                .andExpect(model().attributeExists("provider"))
//                .andExpect(model().attribute("provider", new BusinessProvider()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("When POST /business_provider/create then return business provider form submitted")
    @Disabled
    void whenPostBusinessProviderCreate_thenReturnBusinessProviderFormSubmitted() throws Exception {
        //given
        given(service.create(any(BusinessProvider.class))).willReturn(provider);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/business_providers/create")
                        .content(new ObjectMapper().writeValueAsString(provider))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("redirect:/business_providers/"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("When GET /business_provider/edit/{id} with valid id then return business provider form")
    void whenGetBusinessProviderEditWithValidId_thenReturnBusinessProviderForm() throws Exception {
        //when
        given(service.getById(any(Long.class))).willReturn(Optional.of(provider));
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/edit/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("businessProvidersPages/edit-business-providers"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attribute("provider", provider))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("John");
    }

    @Test
    @DisplayName("When GET /business_provider/edit/{id} with invalid id then return error 404")
    void whenGetBusinessProviderEditWithInvalidId_thenReturnError404() throws Exception {
        //when
        given(service.getById(any(Long.class))).willReturn(Optional.empty());
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/edit/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("error/error404"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attribute("provider", Optional.empty()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("When POST /business_provider/edit/{id} with valid id then return business provider form submitted")
    @Disabled
    void whenPostBusinessProviderEditWithValidId_thenReturnBusinessProviderFormSubmitted() throws Exception {
        //given
        given(service.update(any(Long.class),any(BusinessProvider.class))).willReturn(provider);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/business_providers/edit/{id}", 1L)
                        .content(new ObjectMapper().writeValueAsString(provider))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("redirect:/business_providers/"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("When POST /business_provider/edit/{id} with invalid id then return error 404")
    void whenPostBusinessProviderEditWithInvalidId_thenReturnError404() throws Exception {
        //given
        given(service.update(any(Long.class),any(BusinessProvider.class))).willReturn(null);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/business_providers/edit/{id}", 1L)
                        .content(new ObjectMapper().writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("error/error404"))
                .andExpect(model().attributeExists("provider"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("When GET /business_provider/delete/{id} with valid id then return true")
    void whenGetBusinessProviderDeleteWithValidId_thenReturnTrue() throws Exception {
        //when
        given(service.delete(any(Long.class))).willReturn(true);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/delete/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("redirect:/business_providers/"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("When GET /business_provider/delete/{id} with invalid id then return false")
    void whenGetBusinessProviderDeleteWithInvalidId_thenReturnFalse() throws Exception {
        //when
        given(service.delete(any(Long.class))).willReturn(false);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/delete/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("error/error404"))
                .andExpect(model().attributeDoesNotExist("provider"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    void whenGetDirectBusiness_thenReturnListOfDirectBusiness() throws Exception {
        //when
        given(service.getById(any(Long.class))).willReturn(Optional.of(provider));
        given(service.findByIdBusinessProvider(any(Long.class))).willReturn(List.of(business));
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/DirecteBusinesses/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("businessProvidersPages/apporteur-daffaires-liste-des-affaires-directes"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attributeExists("businesses"))
//                .andExpect(model().attribute("provider", List.of(provider)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("John");
    }

    @Test
    void getSponsorOfBusinessProvider() throws Exception{
        //when
        given(service.getById(any(Long.class))).willReturn(Optional.of(provider));
        given(service.getProviderAndAllSponsors(any(Long.class))).willReturn(List.of(provider));
        given(parameterService.getApplicationParameters()).willReturn(parameter);

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/ParrainsOfProvider/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("businessProvidersPages/apporteur-daffaires-liste-des-parrains"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attributeExists("providers"))
                .andExpect(model().attributeExists("parameter"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("John");
    }
}
