package net.shvdy.sbproject.controller;

import net.shvdy.sbproject.dto.UserDTO;
import net.shvdy.sbproject.service.UserService;
import net.shvdy.sbproject.service.exception.AccountAlreadyExistsException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 23.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Controller
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
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

    @ModelAttribute("user")
    public UserDTO userRegistrationDto() {
        return new UserDTO();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @PostMapping("/signup")
    public String createAccount(@ModelAttribute("user") @Valid UserDTO userDto, BindingResult result) {
        try {
            userService.saveNewUser(userDto);
        } catch (AccountAlreadyExistsException e) {
            result.rejectValue("username", "", "The account is already existing for this email");
        }
        if (result.hasErrors())
            return "signup";
        return "redirect:/login?success";
    }
}