package net.shvdy.sbproject.controller;

import net.shvdy.sbproject.dto.UserDTO;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.entity.UserProfile;
import net.shvdy.sbproject.service.UserService;
import net.shvdy.sbproject.service.exception.AccountAlreadyExistsException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
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

    @RequestMapping("/signup")
    public String signUpPage(Authentication auth) {
        if (auth != null && auth.isAuthenticated())
            return "redirect:/";
        return "signup";
    }

    @PostMapping("/signup")
    public String createAccount(UserDTO userDto, Model model) {
        try {
            userService.saveNewUser(userDto);
        } catch (AccountAlreadyExistsException e) {
            model.addAttribute("error", "The account is already existing for this email");
            return "signup";
        }
        return "redirect:/login?signedup";
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
}