package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.controllers.RatingController;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@AutoConfigureMockMvc
@EnableWebMvc
@SpringBootTest(classes = RatingController.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private RatingService ratingService;

    private List<Rating> ratingList;


    @BeforeEach
    void setUp(){
        ratingList = new ArrayList<>(Arrays.asList(
                new Rating(),
                new Rating()
        ));
    }


    @Test
    @WithMockUser
    public void getRatingList() throws Exception {

        when(ratingService.findAll()).thenReturn(ratingList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rating/list").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attribute("ratings", ratingList));

    }

    @Test
    @WithMockUser
    public void validateRating() throws Exception {

        Rating rating = new Rating();
        when(ratingService.save(any(Rating.class))).thenReturn(rating);

        mockMvc.perform(post("/rating/validate").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ratingList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }






    @Test
    @WithMockUser
    void updateRating() throws Exception {
        Rating ratingExisted = new Rating();
        ratingExisted.setId(1);
        ratingExisted.setSandPRating("Test");

        Rating updatedRating = new Rating();
        updatedRating.setSandPRating("New Test");

        when(ratingService.save(any(Rating.class))).thenReturn(updatedRating);

        mockMvc.perform(post("/rating/update/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRating))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser
    void testDeleteRating() throws Exception {
        doNothing().when(ratingService).delete(anyInt());

        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).delete(anyInt());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
