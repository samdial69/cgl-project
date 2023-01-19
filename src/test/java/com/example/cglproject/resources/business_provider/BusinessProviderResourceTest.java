package com.example.cglproject.resources.business_provider;

import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.services.business_provider.BusinessProviderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
    private BusinessProvider businessProvider;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessProviderServiceImpl service;

    @BeforeEach
    void setUp() {
        this.businessProvider = BusinessProvider.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .isSponsored(false)
                .build();
    }

    @Test
    @DisplayName("When GET /business_provider is not empty then return list of business providers")
    void whenGetBusinessProviderIsNotEmpty_thenReturnListOfBusinessProviders() throws Exception {
        //given
        List<BusinessProvider> businessProviders = List.of(businessProvider);
        //when
        given(service.getAllBusinessProviders()).willReturn(businessProviders);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/"))
                .andDo(print())
                .andExpect(view().name("business_provider/index"))
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
                .andExpect(view().name("business_provider/index"))
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
        given(service.getById(any(Long.class))).willReturn(Optional.of(businessProvider));
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("business_provider/show"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attribute("provider", businessProvider))
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
                .andExpect(view().name("errors/error404"))
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
                .andExpect(view().name("business_provider/add"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attribute("provider", new BusinessProvider()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("When POST /business_provider/create then return business provider form submitted")
    void whenPostBusinessProviderCreate_thenReturnBusinessProviderFormSubmitted() throws Exception {
        //given
        given(service.create(any(BusinessProvider.class))).willReturn(businessProvider);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/business_providers/create")
                        .content(new ObjectMapper().writeValueAsString(businessProvider))
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
        given(service.getById(any(Long.class))).willReturn(Optional.of(businessProvider));
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/business_providers/edit/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("business_provider/edit"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attribute("provider", businessProvider))
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
                .andExpect(view().name("errors/error404"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attribute("provider", Optional.empty()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("When POST /business_provider/edit/{id} with valid id then return business provider form submitted")
    void whenPostBusinessProviderEditWithValidId_thenReturnBusinessProviderFormSubmitted() throws Exception {
        //given
        given(service.update(any(Long.class),any(BusinessProvider.class))).willReturn(businessProvider);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/business_providers/edit/{id}", 1L)
                        .content(new ObjectMapper().writeValueAsString(businessProvider))
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
                        .content(new ObjectMapper().writeValueAsString(businessProvider))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("errors/error404"))
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
                .andExpect(view().name("errors/error404"))
                .andExpect(model().attributeDoesNotExist("provider"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }
}
