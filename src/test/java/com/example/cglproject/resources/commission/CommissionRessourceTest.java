package com.example.cglproject.resources.commission;

import com.example.cglproject.models.Business;
import com.example.cglproject.models.BusinessProvider;
import com.example.cglproject.models.Commission;
import com.example.cglproject.resources.parameter.ParameterResource;
import com.example.cglproject.services.comission.CommissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
@WebMvcTest(CommissionRessource.class)
@ContextConfiguration(classes = {CommissionRessource.class})
class CommissionRessourceTest {

    private Commission commission;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommissionServiceImpl service;

    @BeforeEach
    void setUp() {
        commission = Commission.builder()
                .id(1L)
                .commission(1000)
//                .recipient(BusinessProvider.builder().build())
//                .business(Business.builder().build())
                .build();
    }

    @Test
    @DisplayName("When GET /commissions/ then return all commissions")
    void whenGetAllCommissions_ThenReturnAllCommissions() throws Exception {
        //given
        List<Commission> commissions = List.of(commission);

        //when
        given(service.getAllCommissions()).willReturn(commissions);


        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/commissions/"))
                .andDo(print())
                .andExpect(view().name("commission/index"))
                .andExpect(model().attributeExists("commissions"))
                .andExpect(model().attribute("commissions", commissions))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("When GET /commissions/ then return empty list")
    void whenGetAllCommissions_ThenReturnEmptyList() throws Exception {
        //given

        //when
        given(service.getAllCommissions()).willReturn(List.of());

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/commissions/"))
                .andDo(print())
                .andExpect(view().name("commission/index"))
                .andExpect(model().attributeExists("commissions"))
                .andExpect(model().attribute("commissions", List.of()))
                .andReturn().getResponse();

    }
    @Test
    @DisplayName("When GET /commissions/{id} then return commission")
    void whenGetCommissionById_ThenReturnCommission() throws Exception {

        //when
        given(service.findById(any(Long.class))).willReturn(Optional.of(commission));

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/commissions/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("commission/show"))
                .andExpect(model().attributeExists("commission"))
                .andExpect(model().attribute("commission", commission))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    @DisplayName("When GET /commissions/{id} then return 404")
    void whenGetCommissionById_ThenReturn404() throws Exception {

        //when
        given(service.findById(any(Long.class))).willReturn(Optional.empty());

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/commissions/{id}", 1L))
                .andDo(print())
                .andExpect(view().name("error/error404"))
                .andExpect(model().attributeDoesNotExist("commissions"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("Error");
    }

    @Test
    @DisplayName("When GET /commissions/create then return commission form")
    void whenGetCreateCommission_ThenReturnCommissionForm() throws Exception {

        //when
//        given(service.findById(any(Long.class))).willReturn(Optional.of(commission));

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/commissions/create"))
                .andDo(print())
                .andExpect(view().name("commission/add"))
                .andExpect(model().attributeExists("commission"))
//                .andExpect(model().attribute("commission", commission))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }
}
