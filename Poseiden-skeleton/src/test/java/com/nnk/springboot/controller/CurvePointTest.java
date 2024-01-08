package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.ICurvePointService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
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
@SpringBootTest(classes = CurveController.class)
@Slf4j
@WithMockUser
public class CurvePointTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ICurvePointService curvePointService;


    private List<CurvePoint> curvePointList;

    @Mock
    private Model model;

    @BeforeEach
    void setUp(){
        curvePointList = new ArrayList<>(Arrays.asList(
                new CurvePoint(1, 2.2, 100.0),
                new CurvePoint(2, 2.2, 200.0)
        ));
    }

    @Test
    @WithMockUser
    public void getCurvePoint() throws Exception {


        // Mock the service method to return the mock data
        when(curvePointService.findAll()).thenReturn(curvePointList);

        // Perform the MVC request and assert the results
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/curvePoint/list").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attribute("curvePoints", curvePointList));

    }

    @Test
    @WithMockUser
    public void validateCurvePoint() throws Exception {

        CurvePoint curvePoint = new CurvePoint(3, 2.2, 200.0);
        curvePointList.add(curvePoint);
        when(curvePointService.save(any(CurvePoint.class))).thenReturn(curvePoint);

        mockMvc.perform(post("/curvePoint/validate").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(curvePoint))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser
    void updateCurve() throws Exception {
        CurvePoint curvePointExisted = new CurvePoint();
        curvePointExisted.setCurveId(1);
        curvePointExisted.setTerm(20.0);

        CurvePoint updatedCurvePoint = new CurvePoint();
        updatedCurvePoint.setTerm(50.0); // Set any updated fields

        when(curvePointService.save(any(CurvePoint.class))).thenReturn(updatedCurvePoint);

        mockMvc.perform(post("/curvePoint/update/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCurvePoint))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }



    @Test
    @WithMockUser
    void testDeleteCurvePoint() throws Exception {
        doNothing().when(curvePointService).delete(anyInt());

        // Act & Assert
        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(redirectedUrl("/curvePoint/list"));

        // Verify that the delete method is called with the correct id
        verify(curvePointService, times(1)).delete(anyInt());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
