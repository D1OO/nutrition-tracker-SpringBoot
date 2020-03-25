package net.shvdy.sbproject.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.shvdy.sbproject.dto.UserDTO;
import net.shvdy.sbproject.entity.RoleType;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.entity.UserProfile;
import net.shvdy.sbproject.repository.UserRepository;
import net.shvdy.sbproject.service.exception.AccountAlreadyExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveNewUser(UserDTO userDTO) throws AccountAlreadyExistsException {
        User newUser = DTOToEntityMapper(userDTO);
        newUser.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new AccountAlreadyExistsException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.findByUsername(email).orElseThrow(() ->
                new UsernameNotFoundException("user " + email + " was not found!"));
    }

    private User DTOToEntityMapper(UserDTO userDTO) {
        User u = new User();
        BeanUtils.copyProperties(userDTO, u);
        u.setUserProfile(new UserProfile());
        u.getUserProfile().setUser(u);
        u.getUserProfile().setUserFood(new ArrayList<>());
        u.setAccountNonExpired(true);
        u.setAccountNonLocked(true);
        u.setEnabled(true);
        u.setCredentialsNonExpired(true);
        u.setAuthorities(Collections.singleton(RoleType.ROLE_USER));
        return u;
    }
}
