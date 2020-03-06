/**
 * PageController
 * <p>
 * version 1.0
 * <p>
 * 06.03.2020
 * <p>
 * Copyright(r) shvdy.net
 */

package net.shvdy.sbproject.controller;

import net.shvdy.sbproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    private final UserService userService;


    @Autowired
    public PageController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("users", userService.getAllUsers().getUsers());
        return "index.html";
    }
}
