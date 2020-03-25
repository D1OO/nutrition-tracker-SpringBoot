package net.shvdy.sbproject.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.shvdy.sbproject.dto.UserDTO;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.repository.UserRepository;
import net.shvdy.sbproject.service.exception.AccountAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void saveNewUser(UserDTO userDTO) throws AccountAlreadyExistsException {
        User newUser = modelMapper.map(userDTO, User.class);
        newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new AccountAlreadyExistsException();
        }
    }

    public void updateProfile(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.findByUsername(email).orElseThrow(() ->
                new UsernameNotFoundException("user " + email + " was not found!"));
    }
}
