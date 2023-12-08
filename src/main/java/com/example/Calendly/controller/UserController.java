package com.example.Calendly.controller;

import com.example.Calendly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signUp")
    public String signUp(){
        return "signUp";
    }
    @GetMapping("/signIn")
    public String signIn(){
        return "signIn";
    }

    @PostMapping("/saveRegisteredUser")
    public String saveRegisteredUser(@RequestParam("name") String name,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password, Model model){

        if(userService.findUserByEmail(email)!=null){
            model.addAttribute("emailExists", true);
            return "signUp";
        }

        userService.save(name,email,"{noop}"+password);

        return "signIn";
    }
}
