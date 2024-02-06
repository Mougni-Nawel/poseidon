package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userRepository.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    @GetMapping("/403")
    public String error(Model model) {
        String errorMessage= "You are not authorized for the requested data.";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String remoteUser = authentication.getName();
        model.addAttribute("userDetails", remoteUser);
        model.addAttribute("errorMsg", errorMessage);;
        return "403";
    }
}
