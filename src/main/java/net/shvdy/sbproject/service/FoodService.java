package net.shvdy.sbproject.service;

import net.shvdy.sbproject.dto.FoodDTO;
import net.shvdy.sbproject.entity.Food;
import net.shvdy.sbproject.entity.UserProfile;
import net.shvdy.sbproject.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 20.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Service
public class FoodService {
    FoodRepository foodRepository;
    FoodUtils foodUtils;
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public FoodService(FoodRepository foodRepository, FoodUtils foodUtils) {
        this.foodRepository = foodRepository;
        this.foodUtils = foodUtils;
    }

    public Food saveNewFood(FoodDTO foodDTO) {
        Food food = foodUtils.mapDTOToEntity(foodDTO);
        food.setUserProfile(entityManager.getReference(UserProfile.class, foodDTO.getProfileId()));
        return foodRepository.save(food);
    }
}