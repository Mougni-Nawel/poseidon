package com.nnk.springboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@EnableWebMvc
@SpringBootTest(classes = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;



    private List<User> userList;

    private User userTest;


    @BeforeEach
    void setUp(){
        userList = new ArrayList<>(Arrays.asList(
                new User(),
                new User()
        ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getUserList() throws Exception {

        when(userService.findAll()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/list").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attribute("users", userList));

    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAddUser() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateUser_ValidUser_RedirectToList() throws Exception {

        when(userService.save(any())).thenReturn(userTest);

        mockMvc.perform(post("/user/validate").with(csrf())
                        .param("username", "testUser")
                        .param("password", "testPassword123!")
                        .param("fullname", "Test")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTrade() throws Exception {
        User userExisted = new User();
        userExisted.setId(10);
        userExisted.setFullname("Test");
        userExisted.setRole("USER");
        userExisted.setPassword("testPassword0?");
        userExisted.setUsername("testUsername");

        User updatedUser = new User();
        updatedUser.setFullname("New Test");
        updatedUser.setRole("USER");
        updatedUser.setPassword("testPassword123!");
        updatedUser.setUsername("testUsername");

        when(userService.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/user/update/"+userExisted.getId()).with(csrf())
                        .param("fullname", updatedUser.getFullname())
                        .param("role", updatedUser.getFullname())
                        .param("username", updatedUser.getUsername())
                        .param("password", updatedUser.getPassword())


                )
                .andExpect(redirectedUrl("/user/list"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteTrade() throws Exception {

        userTest = new User();
        userTest.setId(1);
        userTest.setRole("USER");
        userTest.setFullname("Test");
        userTest.setUsername("Test");
        userTest.setPassword("testPassword123!");

        when(userRepository.findById(userTest.getId())).thenReturn(Optional.ofNullable(userTest));
        doNothing().when(userRepository).delete(any(User.class));

        mockMvc.perform(get("/user/delete/1"))
                .andExpect(redirectedUrl("/user/list"));

        verify(userRepository, times(1)).delete(any(User.class));
    }








    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
