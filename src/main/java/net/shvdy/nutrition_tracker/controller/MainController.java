package net.shvdy.nutrition_tracker.controller;

import net.shvdy.nutrition_tracker.dto.UserProfileDTO;
import net.shvdy.nutrition_tracker.model.entity.UserProfile;
import net.shvdy.nutrition_tracker.model.service.Mapper;
import net.shvdy.nutrition_tracker.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
    UserProfileDTO editUserProfile() {
        return Mapper.MODEL.map(sessionInfo.getUser().getUserProfile(), UserProfileDTO.class);
    }

    @RequestMapping("/user")
    public String userPage() {
        return "user";
    }

    @RequestMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @RequestMapping("/profile")
    public String userProfile() {
        return "fragments/user-page/profile :: content";
    }

    @PostMapping("/profile")
    public ResponseEntity<String> updateProfile(@Valid UserProfileDTO userProfile) {
        UserProfile updatedProfile = Mapper.MODEL.map(userProfile, UserProfile.class);
        updatedProfile.setProfileId(sessionInfo.getUser().getUserProfile().getProfileId());
        sessionInfo.getUser().setUserProfile(userService.updateUserProfile(updatedProfile));

        return new ResponseEntity<>("profile?saved", HttpStatus.FOUND);
    }

}