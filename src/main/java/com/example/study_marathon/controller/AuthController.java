package com.example.study_marathon.controller;

import com.example.study_marathon.dto.UserRegisterForm;
import com.example.study_marathon.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserRegistrationService userRegistrationService;

    public AuthController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        if (!model.containsAttribute("userRegisterForm")) {
            model.addAttribute("userRegisterForm", new UserRegisterForm());
        }
        return "auth/login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegisterForm") UserRegisterForm form,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        try {
            userRegistrationService.registerNewUser(form);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("username", "duplicate", e.getMessage());
            return "auth/login";
        }

        model.addAttribute("registerSuccess", true);
        return "auth/login";
    }
}
