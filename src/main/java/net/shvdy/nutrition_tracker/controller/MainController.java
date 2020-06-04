package net.shvdy.nutrition_tracker.controller;

import net.shvdy.nutrition_tracker.entity.UserProfile;
import net.shvdy.nutrition_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    private final UserService userService;
    private final SessionInfo sessionInfo;

    @Autowired
    public MainController(UserService userService, SessionInfo sessionInfo) {
        this.userService = userService;
        this.sessionInfo = sessionInfo;
    }

    @ModelAttribute("userProfile")
    UserProfile editUserProfile() {
        return sessionInfo.getUser().getUserProfile();
    }

    @RequestMapping("/user")
    public String userPage() {
        return "user";
    }

    @RequestMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getUsersList());
        return "admin";
    }

    @RequestMapping("/profile")
    public String userProfile() {
        return "fragments/user-page/profile :: content";
    }

    @PostMapping("/update-profile")
    public String saveProfile(UserProfile userProfile) {
        userService.updateUserProfile(userProfile);
        sessionInfo.getUser().setUserProfile(userProfile);
        return "redirect:/";
    }
}