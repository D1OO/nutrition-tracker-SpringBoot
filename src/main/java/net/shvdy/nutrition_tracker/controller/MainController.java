package net.shvdy.nutrition_tracker.controller;

import net.shvdy.nutrition_tracker.entity.User;
import net.shvdy.nutrition_tracker.entity.UserProfile;
import net.shvdy.nutrition_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/user")
    public String userPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user";
    }

    @RequestMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getUsersList());
        return "admin";
    }

    @RequestMapping("/profile")
    public String userProfile(@AuthenticationPrincipal User user, Model model) {
        UserProfile userProfile = userService.getUserProfile(user.getId());
        model.addAttribute("userProfile", userProfile);
        return "fragments/user-page/profile :: content";
    }

    @PostMapping("/update-profile")
    public String saveProfile(UserProfile userProfileDTO, @AuthenticationPrincipal User user) {
        userService.updateUserProfile(userProfileDTO);
        user.setUserProfile(userProfileDTO);
        userProfileDTO.setUser(user);
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(user,
                user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }
}