package net.shvdy.nutrition_tracker.service;

import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.entity.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 28.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Service
public class ServiceUtils {

    private Mapper mapper;

    @Autowired
    public ServiceUtils(Mapper mapper) {
        this.mapper = mapper;
    }

    public Food getFoodFromJSON(String foodDTOJSON) throws IOException {
        return mapper.getModelMapper().map(mapper.getJacksonMapper().readValue(foodDTOJSON, FoodDTO.class), Food.class);
    }

    public List<FoodDTO> FoodListEntityToDTO(List<Food> entities) {
        return entities
                .stream()
                .map(source -> mapper.getModelMapper().map(source, FoodDTO.class))
                .collect(Collectors.toList());
    }

    public Food mapFoodDTOToEntity(FoodDTO foodDTO) {
        return mapper.getModelMapper().map(foodDTO, Food.class);
    }

}
