package net.shvdy.nutrition_tracker.controller;

import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.dto.UserDTO;
import net.shvdy.nutrition_tracker.model.service.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
    public ResponseEntity<String> createAccount(@Valid UserDTO userDto) {
        try {
            userService.saveNewUser(userDto);
            return new ResponseEntity<>("login?signedup", HttpStatus.FOUND);
        } catch (Exception e) {
            log.warn("The account already exists for email: " + userDto.getUsername());
            return new ResponseEntity<>("signup?error", HttpStatus.BAD_REQUEST);
        }
    }

}