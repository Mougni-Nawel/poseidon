package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.controllers.TradeController;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
@SpringBootTest(classes = TradeController.class)
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private TradeService tradeService;

    private List<Trade> tradeList;


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

        when(tradeService.findAll()).thenReturn(tradeList);

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
        updatedTrade.setType("New Test");

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

        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(redirectedUrl("/trade/list"));

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
