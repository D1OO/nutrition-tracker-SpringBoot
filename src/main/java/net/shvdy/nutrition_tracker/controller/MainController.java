package net.shvdy.nutrition_tracker.controller;

import net.shvdy.nutrition_tracker.entity.UserProfile;
import net.shvdy.nutrition_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
//        model.addAttribute("users", userService.getUsersList());
        return "admin";
    }

    @RequestMapping("/profile")
    public String userProfile() {
        return "fragments/user-page/profile :: content";
    }

    @PostMapping("/profile")
    public String saveProfile(@Valid UserProfile userProfile) {
        sessionInfo.getUser().setUserProfile(userService.updateUserProfile(userProfile));
        return "redirect:/";
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(BindException ex) {
        return new ResponseEntity<>(ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(x -> ((FieldError) x).getField() + "Error",
                        y -> Optional.ofNullable(y.getDefaultMessage()).orElse(""))), HttpStatus.BAD_REQUEST);
    }
}