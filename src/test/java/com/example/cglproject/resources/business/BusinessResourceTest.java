package com.example.cglproject.resources.business;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.models.Commission;
import com.example.cglproject.services.business.BusinessServiceImpl;
import com.example.cglproject.services.business_provider.IBusinesProviderService;
import com.example.cglproject.services.comission.ICommissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusinessResource.class)
@ContextConfiguration(classes = {BusinessResource.class})
class BusinessResourceTest {

    private Business business;
    private BusinessProvider provider;
    private Commission commission;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessServiceImpl service;

    @MockBean
    private IBusinesProviderService providerService;

    @MockBean
    private ICommissionService commissionService;

    @BeforeEach
    void setUp() {


        this.provider = BusinessProvider.builder()
                .id(1L)
                .lastname("lastname")
                .firstname("firstname")
                .sponsor(this.provider)
//                .businesses(List.of(business))
//                .commissions(List.of(commission))
//                .sponsored(List.of(provider))
                .build();

        this.commission = Commission.builder()
                .id(1L)
                .commission(1000)
//                .business(this.business)
                .recipient(this.provider)
                .build();

        this.business = Business.builder()
                .id(1L)
                .title("title")
                .createdAt(LocalDate.now())
                .commissions(List.of(commission))
                .provider(provider)
                .build();


    }

    @Test
    void getBusinesses() throws Exception {
        //given
        List<Business> businesses = List.of(business);
        //when
        when(service.getAllBusinesses()).thenReturn(businesses);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/businesses/"))
                .andDo(print())
                .andExpect(view().name("businessPages/tous-les-affaires"))
                .andExpect(model().attributeExists("businesses"))
                .andExpect(model().attribute("businesses", businesses))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("title");



    }

    @Test
    @DisplayName("Get businesses return empty list")
    void getBusinessesReturnEmptyList() throws Exception {
        //when
        when(service.getAllBusinesses()).thenReturn(List.of());
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/businesses/"))
                .andDo(print())
                .andExpect(view().name("businessPages/tous-les-affaires"))
                .andExpect(model().attributeExists("businesses"))
                .andExpect(model().attribute("businesses",List.of()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("Get business by id")
    void getBusinessById() throws Exception {
        //when
        when(service.findById(1L)).thenReturn(Optional.of(business));
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/businesses/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("businessPages/edit-business"))
                .andExpect(model().attributeExists("business"))
                .andExpect(model().attribute("business", business))
                .andReturn().getResponse();

        assertThat(business.getId()).isEqualTo(1L);
        assertThat(business.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("Get business by id return 404 not found")
    void givenFakeId_whenGetBusiness_thenReturn404NotFound() throws Exception {
        //when
        when(service.findById(1L)).thenReturn(Optional.empty());
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/businesses/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("error/error404"))
                .andExpect(model().attributeDoesNotExist("business"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("Error");
    }

    @Test
    @DisplayName("Get business form")
    void createForm() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/businesses/create"))
                .andDo(print())
                .andExpect(view().name("businessPages/add-business"))
                .andExpect(model().attributeExists("business"))
//                .andExpect(model().attribute("business", new Business()))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(200);
        Assertions.assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");

    }

    @Test
    @DisplayName("Create business form submission")
    void create() throws Exception {
        //given
        double initialCommission = 1000;
        //when
//        when(service.create(business)).thenReturn(business);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/businesses/create")
                        .param("title", business.getTitle())
                        .param("initialCommission", String.valueOf(initialCommission))
                        .param("providerId", String.valueOf(business.getProvider().getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("redirect:/businesses/"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("Get Update business form with valid id")
    void updateForm() throws Exception {
        //when
        when(service.findById(1L)).thenReturn(Optional.of(business));
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/businesses/edit/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("businessPages/edit-business"))
                .andExpect(model().attributeExists("business"))
                .andExpect(model().attributeExists("provider"))
                .andExpect(model().attribute("business", business))
                .andReturn().getResponse();

        assertThat(business.getId()).isEqualTo(1L);
        assertThat(business.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("Get Update business form with invalid id")
    void updateFormWithInvalidId() throws Exception {
        //when
        when(service.findById(1L)).thenReturn(Optional.empty());
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/businesses/edit/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("error/error404"))
                .andExpect(model().attributeExists("business"))
                .andExpect(model().attribute("business", Optional.empty()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("Error");
    }

    @Test
    @DisplayName("Update business form submission success")
    @Disabled
    void update() throws Exception {
        //when
        when(service.update(any(Long.class),any(Business.class))).thenReturn(business);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/businesses/edit/{id}", 1L)
                        .content(new ObjectMapper().writeValueAsString(business))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("redirect:/businesses/"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("Update business form submission failure")
    @Disabled
    void updateFailure() throws Exception {
        //when
        when(service.update(any(Long.class),any(Business.class))).thenReturn(null);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/businesses/edit/{id}", 1L)
                        .content(new ObjectMapper().writeValueAsString(business))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("errors/error404"))
                .andExpect(model().attributeExists("business"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("Error");
    }

    @Test
    @DisplayName("Delete business with valid id")
    void delete() throws Exception {
        //when
        when(service.delete(any(Long.class))).thenReturn(true);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/businesses/delete/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("redirect:/businesses/"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("Delete business with invalid id")
    void deleteWithInvalidId() throws Exception {
        //when
        when(service.delete(any(Long.class))).thenReturn(false);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/businesses/delete/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("error/error404"))
                .andExpect(model().attributeDoesNotExist("business"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("Error");
    }


}
