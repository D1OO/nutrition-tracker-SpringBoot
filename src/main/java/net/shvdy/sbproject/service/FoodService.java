package net.shvdy.sbproject.service;

import net.shvdy.sbproject.dto.FoodDTO;
import net.shvdy.sbproject.entity.Food;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 20.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Service
public class FoodService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public FoodService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<FoodDTO> getUsersFood(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Food> userFood = user.get().getUserProfile().getUserFood();
            return entityToDTO(userFood);
        } else
            return new ArrayList<>();
    }

    public HashMap<Long, FoodDTO> convertListToHashMapOnFoodId(List<FoodDTO> foodList) {
        return new HashMap<>(foodList.stream()
                .collect(Collectors.toMap(FoodDTO::getFood_id, Function.identity())));
    }

    private List<FoodDTO> entityToDTO(List<Food> entities) {
        return entities
                .stream()
                .map(source -> modelMapper.map(source, FoodDTO.class))
                .collect(Collectors.toList());
    }
}