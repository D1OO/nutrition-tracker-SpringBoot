package net.shvdy.sbproject.controller;

import lombok.extern.log4j.Log4j2;
import net.shvdy.sbproject.dto.UserDTO;
import net.shvdy.sbproject.service.UserService;
import net.shvdy.sbproject.service.exception.AccountAlreadyExistsException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 23.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Controller
@Log4j2
public class SecurityController {
    private final UserService userService;

    public SecurityController(UserService userService) {
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

    @RequestMapping("/signup")
    public String signUpPage() {
        return "signup";
    }

    @RequestMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/access-denied")
    public String accessDenied() {
        return "redirect:/";
    }

    @PostMapping("/signup")
    public String createAccount(UserDTO userDto) {
        try {
            userService.saveNewUser(userDto);
        } catch (AccountAlreadyExistsException e) {
            log.warn("The account already exists for this email address");
            return "signup?error";
        }
        return "redirect:/login?signedup";
    }

}