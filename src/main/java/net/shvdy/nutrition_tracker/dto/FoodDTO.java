package net.shvdy.nutrition_tracker.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.model.service.Mapper;
import org.hibernate.validator.constraints.Length;

/**
 * 21.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class FoodDTO {
    private Long food_id;
    @Length(min = 2)
    private String name;
    private int calories;
    private int proteins;
    private int fats;
    private int carbohydrates;

    @Override
    public String toString() {
        try {
            return Mapper.JACKSON.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: " + e);
            return "";
        }
    }
}
