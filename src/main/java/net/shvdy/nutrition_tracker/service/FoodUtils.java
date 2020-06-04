package net.shvdy.nutrition_tracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.entity.Food;
import net.shvdy.nutrition_tracker.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
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
public class FoodUtils {

    private ObjectMapper jacksonMapper;
    private ModelMapper modelMapper;

    @Autowired
    public FoodUtils(ObjectMapper jacksonMapper, ModelMapper modelMapper) {
        this.jacksonMapper = jacksonMapper;
        this.modelMapper = modelMapper;
    }


    public Food getFoodFromJSON(String foodDTOJSON) throws IOException {
        return modelMapper.map(jacksonMapper.readValue(foodDTOJSON, FoodDTO.class), Food.class);
    }


    public List<FoodDTO> entityToDTO(List<Food> entities) {
        return entities
                .stream()
                .map(source -> modelMapper.map(source, FoodDTO.class))
                .collect(Collectors.toList());
    }

//    public HashMap<Long, Food> convertJSONToObjects(String foodListJSON) {
//        FoodContainer foodContainer;
//        try {
//            foodContainer = jacksonMapper.readValue(foodListJSON, FoodContainer.class);
//        } catch (IOException e) {
//            foodContainer = new FoodContainer(Collections.emptyList());
//        }
//        return  convertListToHashMapOnFoodId(foodContainer.getUserFood());
//    }

    public Food mapDTOToEntity(FoodDTO foodDTO) {
        return modelMapper.map(foodDTO, Food.class);
    }

    public void updateAuthenticatedUser(User user) {
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(user,
                user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
