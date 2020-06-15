package net.shvdy.nutrition_tracker.model.service;

import lombok.NonNull;
import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.dto.UserDTO;
import net.shvdy.nutrition_tracker.model.entity.Food;
import net.shvdy.nutrition_tracker.model.entity.User;
import net.shvdy.nutrition_tracker.model.entity.UserProfile;
import net.shvdy.nutrition_tracker.model.repository.UserProfileRepository;
import net.shvdy.nutrition_tracker.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;

    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.findByUsername(email).orElseThrow(() ->
                new UsernameNotFoundException("user " + email + " was not found!"));
    }

    public void saveNewUser(UserDTO userDTO) {
        User newUser = Mapper.MODEL.map(userDTO, User.class);
        newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        newUser.getUserProfile().setUser(newUser);

        userRepository.save(newUser);
    }

    public UserProfile saveCreatedFood(UserProfile userProfile, FoodDTO foodDTO) {
        userProfile.getUserFood().add(Mapper.MODEL.map(foodDTO, Food.class));
        return userProfileRepository.save(userProfile);
    }

    @Transactional
    public UserProfile updateUserProfile(UserProfile userProfile) {
        return entityManager.merge(userProfile);
    }
}
