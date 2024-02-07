package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;


@AutoConfigureMockMvc
@EnableWebMvc
@SpringBootTest(classes = BidListController.class)
public class BidControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BidListService bidListService;


    private List<BidList> mockBidList;


    @BeforeEach
    void setUp(){
        mockBidList = new ArrayList<>(Arrays.asList(
                new BidList("Account1", "Type1", 100.0),
                new BidList("Account2", "Type2", 200.0)
        ));
    }

    @Test
    @WithMockUser
    public void getBidList() throws Exception {

        when(bidListService.findAll()).thenReturn(mockBidList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bidList/list").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attribute("bidLists", mockBidList));

    }

    @Test
    @WithMockUser
    public void validateBidList() throws Exception {

        BidList bidList = new BidList("Account1", "Type1", 100.0);

        when(bidListService.save(any(BidList.class))).thenReturn(bidList);

        mockMvc.perform(post("/bidList/validate").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(bidList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    void updateBid() throws Exception {
        BidList bidListExisted = new BidList();
        bidListExisted.setBidListId(1);
        bidListExisted.setType("Test");

        BidList updatedBidList = new BidList();
        updatedBidList.setType("New Test");

        when(bidListService.save(any(BidList.class))).thenReturn(updatedBidList);

        mockMvc.perform(post("/bidList/update/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedBidList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    void testDeleteBid() throws Exception {
        doNothing().when(bidListService).delete(anyInt());

        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).delete(anyInt());
    }



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
