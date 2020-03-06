/**
 * UserService
 * <p>
 * version 1.0
 * <p>
 * 06.03.2020
 * <p>
 * Copyright(r) shvdy.net
 */

package net.shvdy.sbproject.service;

import lombok.extern.slf4j.Slf4j;
import net.shvdy.sbproject.dto.UserDTO;
import net.shvdy.sbproject.dto.UsersDTO;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UsersDTO getAllUsers() {
        //TODO checking for an empty user list
        return new UsersDTO(userRepository.findAll());
    }

    public Optional<User> findByUserLogin(UserDTO userDTO) {
        //TODO check for user availability. password check
        return userRepository.findByEmail(userDTO.getEmail());
    }

    public void saveNewUser(User user) {
        //TODO inform the user about the replay email
        // TODO exception to endpoint
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            log.info("{Почтовый адрес уже существует}");
        }

    }
}
