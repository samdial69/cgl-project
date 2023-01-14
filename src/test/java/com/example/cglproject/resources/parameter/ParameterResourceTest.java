package com.example.cglproject.resources.parameter;

import com.example.cglproject.models.Parameter;
import com.example.cglproject.services.parameter.ParameterServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParameterResource.class)
@ContextConfiguration(classes = {ParameterResource.class})
class ParameterResourceTest {
    private Parameter parameter;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParameterServiceImpl service;

    @BeforeEach
    void setUp() {
        this.parameter = Parameter.builder()
                .id(1L)
                .levelOfSponsorship(5)
                .percentageOfCommission(0.2)
                .numberOfBusinessToBeAffiliated(2)
                .numberOfMonthsAffiliation(2)
                .build();
    }

    @Test
    void whenGetParameterIsNotEmpty_thenReturnListOfParameters() throws Exception {
        //given
        List<Parameter> parameters = List.of(parameter);
        //when
        given(service.findAll()).willReturn(parameters);

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/parameters/"))
                .andDo(print())
                .andExpect(view().name("parameter/index"))
                .andExpect(model().attributeExists("parameters"))
                .andExpect(model().attribute("parameters", parameters))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("5");
    }

    @Test
    void whenGetParameterIsEmpty_thenReturnError404() throws Exception {
        //when
        given(service.findAll()).willReturn(List.of());
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/parameters/"))
                .andDo(print())
                .andExpect(view().name("errors/error404"))
                .andExpect(model().attributeDoesNotExist("parameters"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
        assertThat(response.getContentAsString()).contains("Error");
    }

    @Test
    void whenCreateParameter_thenReturnForm() throws Exception {
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/parameters/create"))
                .andDo(print())
                .andExpect(view().name("parameter/add"))
                .andExpect(model().attributeExists("parameter"))
                .andExpect(model().attribute("parameter", new Parameter()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    void whenCreateParameter_thenReturnSuccessAfterSubmit() throws Exception {
        //when
        given(service.save(any(Parameter.class))).willReturn(parameter);

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/parameters/create")
                        .content(new ObjectMapper().writeValueAsString(parameter))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(view().name("redirect:/parameters/"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
    }


    @Test
    void whenEditParameter_thenReturnFormWithParameter() throws Exception {
        //when
        given(service.findById(any(Long.class))).willReturn(Optional.of(parameter));

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/parameters/edit/1"))
                .andDo(print())
                .andExpect(view().name("parameter/edit"))
                .andExpect(model().attributeExists("parameter"))
                .andExpect(model().attribute("parameter", parameter))
                .andReturn().getResponse();

    }

    //TODO: Test when find by id and not found then return 404

    @Test
    @DisplayName("When returning form for edit and parameter not found by the id used")
    void givenFakeId_whenEdit_thenReturn404NotFound() throws Exception {
        //when
        when(service.findById(any(Long.class))).thenReturn(Optional.empty());

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/parameters/edit/{id}", 1))
                .andDo(print())
                .andExpect(view().name("errors/error404"))
                .andExpect(model().attributeDoesNotExist("parameter"))
                .andReturn().getResponse();

    }

    @Test
    @DisplayName("When edit parameter then return success after submit")
    void givenExistingId_whenUpdateParameter_thenReturnSuccess() throws Exception {
        //when
        given(service.update(any(Long.class), any(Parameter.class))).willReturn(parameter);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/parameters/edit/1")
                        .content(new ObjectMapper().writeValueAsString(parameter))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(view().name("redirect:/parameters/"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("Form submit when service didn't find parameter then return 404")
    void givenFakeId_whenSendUpdateParameterForm_thenReturn404NotFound() throws Exception {
        //when
        given(service.update(any(Long.class), any(Parameter.class))).willReturn(null);
        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/parameters/edit/1")
                        .content(new ObjectMapper().writeValueAsString(parameter))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(view().name("errors/error404"))
                .andExpect(model().attributeExists("parameter"))
                .andReturn().getResponse();
    }


    @Test
    @DisplayName("Delete parameter by id")
    void givenExistingId_whenDelete_thenReturnSuccess() throws Exception {
        //when
        given(service.findById(any(Long.class))).willReturn(Optional.of(parameter));
        given(service.delete(any(Long.class))).willReturn(true);

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/parameters/delete/1"))
                .andDo(print())
                .andExpect(view().name("redirect:/parameters/"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();
    }
    @Test
    @DisplayName("When delete parameter with fake id then return 404")
    void givenFakeId_whenDelete_thenReturn404NotFound() throws Exception {
        //when
        when(service.delete(any(Long.class))).thenReturn(false);

        //then
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/parameters/delete/{id}", 1))
                .andDo(print())
                .andExpect(view().name("errors/error404"))
                .andExpect(model().attributeDoesNotExist("parameter"))
                .andReturn().getResponse();

    }
}
