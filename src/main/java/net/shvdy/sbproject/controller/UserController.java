package net.shvdy.sbproject.controller;

import net.shvdy.sbproject.dto.UserDTO;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.entity.UserProfile;
import net.shvdy.sbproject.service.UserService;
import net.shvdy.sbproject.service.exception.AccountAlreadyExistsException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * 23.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("newUser")
    public UserDTO newUserDTO() {
        return new UserDTO();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping("/profile")
    public String userProfile(@AuthenticationPrincipal User user, Model model) {
        UserProfile userProfile = userService.getUserProfile(user.getId());
        model.addAttribute("userProfilee", userProfile);
        return "fragments/user-page/profile :: content";
    }

    @PostMapping("/update-profile")
    public String saveProfile(UserProfile userProfileDTO, @AuthenticationPrincipal User user) {
//        user.setUserProfile(userProfileDTO);
        userProfileDTO.setUserId(user.getId());
        userService.updateUserProfile(userProfileDTO);
        return "redirect:/";
    }

    @RequestMapping(value = "/login")
    public String loginPage(Authentication auth,
                            @RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            @RequestParam(required = false) String signedup,
                            Model model) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("signedup", signedup != null);
        return "login";
    }

    @RequestMapping("/signup")
    public String signUpPage(Authentication auth,
                             @RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "logout", required = false) String logout,
                             Model model) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "signup";
    }

    @PostMapping("/signup")
    public String createAccount(UserDTO userDto) {
        try {
            userService.saveNewUser(userDto);
        } catch (AccountAlreadyExistsException e) {
            return "signup";
//            result.rejectValue("username", "", "The account is already existing for this email");
        }
//        if (result.hasErrors())
        return "redirect:/login?success";
    }
}