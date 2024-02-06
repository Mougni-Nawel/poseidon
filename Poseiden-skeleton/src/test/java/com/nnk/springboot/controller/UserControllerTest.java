package com.nnk.springboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.ICurvePointService;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
@SpringBootTest(classes = UserController.class)
@Slf4j
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;



    private List<User> userList;

    private User userTest;

    @Mock
    private Model model;

    @BeforeEach
    void setUp(){
        userList = new ArrayList<>(Arrays.asList(
                new User(),
                new User()
        ));
    }

    @Test
    //@WithMockUser
    public void getUserList() throws Exception {

        when(userService.findAll()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/list").with(SecurityMockMvcRequestPostProcessors.user("username").password("password").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attribute("users", userList));

    }


    @Test
    public void getAddUser() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/add").with(SecurityMockMvcRequestPostProcessors.user("username").password("password").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

    }

    @Test
    @WithMockUser(username = "testUser", password = "testPassword", roles = "ADMIN")
    public void validateUser_ValidUser_RedirectToList() throws Exception {

        when(userService.save(any())).thenReturn(userTest);

        mockMvc.perform(post("/user/validate").with(csrf())
                        .param("username", "testUser")
                        .param("password", "testPassword")
                        .param("fullname", "Test")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }


    @Test
    @WithMockUser(username = "testUser", password = "testPassword", roles = "ADMIN")
    void updateTrade() throws Exception {
        User userExisted = new User();
        userExisted.setId(10);
        userExisted.setFullname("Test");
        userExisted.setRole("USER");
        userExisted.setPassword("testPassword");
        userExisted.setUsername("testUsername");

        User updatedUser = new User();
        updatedUser.setFullname("New Test");
        updatedUser.setRole("USER");
        updatedUser.setPassword("testPassword");
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
    @WithMockUser(username = "testUser", password = "testPassword", roles = "ADMIN")
    void testDeleteTrade() throws Exception {

        userTest = new User();
        userTest.setId(1);
        userTest.setRole("USER");
        userTest.setFullname("Test");
        userTest.setUsername("Test");
        userTest.setPassword("testpassword");

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
