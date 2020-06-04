package net.shvdy.nutrition_tracker.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.shvdy.nutrition_tracker.dto.UserDTO;
import net.shvdy.nutrition_tracker.entity.RoleType;
import net.shvdy.nutrition_tracker.entity.User;
import net.shvdy.nutrition_tracker.entity.UserProfile;
import net.shvdy.nutrition_tracker.repository.UserProfileRepository;
import net.shvdy.nutrition_tracker.repository.UserRepository;
import net.shvdy.nutrition_tracker.service.exception.AccountAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.modelMapper = modelMapper;
    }

    public void saveNewUser(UserDTO userDTO) throws AccountAlreadyExistsException {
        try {
            userRepository.save(setUpNewUser(userDTO));
        } catch (Exception e) {
            throw new AccountAlreadyExistsException();
        }
    }

    @Transactional
    public void updateUserProfile(UserProfile userProfile) {
        entityManager.merge(userProfile);
    }

    public UserProfile getUserProfile(Long userId) {
        return userProfileRepository.findById(userId).orElse(new UserProfile());
    }

    public List<UserDTO> getUsersList() {
        return modelMapper.map(userRepository.findAll(), new TypeToken<ArrayList<UserDTO>>() {
        }.getType());
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.findByUsername(email).orElseThrow(() ->
                new UsernameNotFoundException("user " + email + " was not found!"));
    }

    private User setUpNewUser(UserDTO userDTO) {
        User newUser = modelMapper.map(userDTO, User.class);
        newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        newUser.setAuthorities(Collections.singleton(RoleType.ROLE_USER));
        newUser.getUserProfile().setUser(newUser);
        newUser.setAccountNonLocked(true);
        newUser.setAccountNonExpired(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);
        return newUser;
    }
}
