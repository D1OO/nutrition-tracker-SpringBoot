package net.shvdy.sbproject.controller;

import net.shvdy.sbproject.dto.UserDTO;
import net.shvdy.sbproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 10.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */

@Controller
@RequestMapping("/signup")
public class UserSignUpController {

    @Autowired
    private UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ModelAttribute("user")
    public UserDTO userRegistrationDto() {
        return new UserDTO();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "signup";
    }

    @PostMapping
    public String createAccount(@ModelAttribute("user") @Valid UserDTO userDto,
                                BindingResult result) {
        try {
            userService.saveNewUser(userDto);
        } catch (Exception e) {
            result.rejectValue("username", "", "The account is already existing for this email");
        }
        if (result.hasErrors()) {
            return "signup";
        }
        return "redirect:/login?signedup";
    }

}

