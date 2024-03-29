package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@EnableWebMvc
@SpringBootTest(classes = RuleNameController.class)
public class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private RuleNameService ruleNameService;


    private List<RuleName> ruleNameList;


    @BeforeEach
    void setUp(){
        ruleNameList = new ArrayList<>(Arrays.asList(
                new RuleName(),
                new RuleName()
        ));
    }

    @Test
    @WithMockUser
    public void getRuleNameList() throws Exception {

        when(ruleNameService.findAll()).thenReturn(ruleNameList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ruleName/list").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attribute("ruleNames", ruleNameList));

    }

    @Test
    @WithMockUser
    public void validateRuleName() throws Exception {

        RuleName ruleName = new RuleName();

        when(ruleNameService.save(any(RuleName.class))).thenReturn(ruleName);

        mockMvc.perform(post("/ruleName/validate").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ruleNameList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser
    void updateRuleName() throws Exception {
        RuleName ruleNameExisted = new RuleName();
        ruleNameExisted.setId(1);
        ruleNameExisted.setName("Test");

        RuleName updatedRuleName = new RuleName();
        updatedRuleName.setName("New Test");

        when(ruleNameService.save(any(RuleName.class))).thenReturn(updatedRuleName);

        mockMvc.perform(post("/ruleName/update/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRuleName))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }


    @Test
    @WithMockUser
    void testDeleteRuleName() throws Exception {
        doNothing().when(ruleNameService).delete(anyInt());

        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).delete(anyInt());
    }




    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
