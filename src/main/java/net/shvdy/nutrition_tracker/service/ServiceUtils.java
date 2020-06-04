package net.shvdy.nutrition_tracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.entity.Food;
import org.modelmapper.ModelMapper;
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

    private ObjectMapper jacksonMapper;
    private ModelMapper modelMapper;

    @Autowired
    public ServiceUtils(ObjectMapper jacksonMapper, ModelMapper modelMapper) {
        this.jacksonMapper = jacksonMapper;
        this.modelMapper = modelMapper;
    }

    public Food getFoodFromJSON(String foodDTOJSON) throws IOException {
        return modelMapper.map(jacksonMapper.readValue(foodDTOJSON, FoodDTO.class), Food.class);
    }

    public List<FoodDTO> FoodListEntityToDTO(List<Food> entities) {
        return entities
                .stream()
                .map(source -> modelMapper.map(source, FoodDTO.class))
                .collect(Collectors.toList());
    }

    public Food mapFoodDTOToEntity(FoodDTO foodDTO) {
        return modelMapper.map(foodDTO, Food.class);
    }

}
