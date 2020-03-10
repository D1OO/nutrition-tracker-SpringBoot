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
import net.shvdy.sbproject.dto.UsersDTO;
import net.shvdy.sbproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping(value = "/")
public class TestPageController {

    private final UserService userService;

    @Autowired
    public TestPageController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public UsersDTO getAllUser() {
        log.info("{}", userService.getAllUsers());
        return userService.getAllUsers();
    }
}
