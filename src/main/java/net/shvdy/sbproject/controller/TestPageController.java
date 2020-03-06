/**
 * TestPageController
 * <p>
 * version 1.0
 * <p>
 * 06.03.2020
 * <p>
 * Copyright(r) shvdy.net
 */

package net.shvdy.sbproject.controller;

import lombok.extern.slf4j.Slf4j;
import net.shvdy.sbproject.dto.UserDTO;
import net.shvdy.sbproject.dto.UsersDTO;
import net.shvdy.sbproject.entity.RoleType;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class TestPageController {

    private final UserService userService;

    @Autowired
    public TestPageController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    //@RequestMapping(value = "login", method = RequestMethod.POST)
    @PostMapping(value = "login")
    public void loginFormController(UserDTO user) {
        log.info("{}", userService.findByUserLogin(user));
        log.info("{}", user);
        userService.saveNewUser(User.builder()
                .firstName("Ann")
                .lastName("Reizer")
                .email("AnnReizer@testing.ua")
                .role(RoleType.ROLE_USER)
                .build());
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public UsersDTO getAllUser() {
        log.info("{}", userService.getAllUsers());
        return userService.getAllUsers();
    }
}
