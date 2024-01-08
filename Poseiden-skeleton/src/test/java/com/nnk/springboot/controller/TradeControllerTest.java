package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.controllers.TradeController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.ICurvePointService;
import com.nnk.springboot.services.TradeService;
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
@SpringBootTest(classes = TradeController.class)
@Slf4j
@WithMockUser
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private TradeService tradeService;

    private List<Trade> tradeList;

    @Mock
    private Model model;

    @BeforeEach
    void setUp(){
        tradeList = new ArrayList<>(Arrays.asList(
                new Trade(),
                new Trade()
        ));
    }

    @Test
    @WithMockUser
    public void getTradeList() throws Exception {


        // Mock the service method to return the mock data
        when(tradeService.findAll()).thenReturn(tradeList);


        // Perform the MVC request and assert the results
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/trade/list").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attribute("trades", tradeList));

    }

    @Test
    @WithMockUser
    public void validateTrade() throws Exception {

        Trade trade = new Trade();
        //mockBidList.add(bidList);
        when(tradeService.save(any(Trade.class))).thenReturn(trade);

        mockMvc.perform(post("/trade/validate").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tradeList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser
    void updateTrade() throws Exception {
        Trade tradeExisted = new Trade();
        tradeExisted.setTradeId(1);
        tradeExisted.setType("Test");

        Trade updatedTrade = new Trade();
        updatedTrade.setType("New Test"); // Set any updated fields

        when(tradeService.save(any(Trade.class))).thenReturn(updatedTrade);

        mockMvc.perform(post("/trade/update/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTrade))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }


    @Test
    @WithMockUser
    void testDeleteTrade() throws Exception {
        doNothing().when(tradeService).delete(anyInt());

        // Act & Assert
        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(redirectedUrl("/trade/list"));

        // Verify that the delete method is called with the correct id
        verify(tradeService, times(1)).delete(anyInt());
    }




    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
