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

import lombok.extern.slf4j.Slf4j;
import net.shvdy.sbproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
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
        return "index";
    }

    @RequestMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "contact";
    }

    @RequestMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers().getUsers());
        return "admin";
    }
}
