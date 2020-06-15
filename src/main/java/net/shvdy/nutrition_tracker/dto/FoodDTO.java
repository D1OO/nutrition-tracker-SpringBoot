package net.shvdy.nutrition_tracker.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.model.service.Mapper;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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

    @NotNull
    @Pattern(regexp = "^[A-Z]((?![ .,'-]$)[a-z .,'-]){2,24}$", message = "{validation.incorrect}")
    private String name;

    @NotNull
    @Min(1)
    @Max(910)
    private Integer calories;

    @NotNull
    @Min(1)
    @Max(40)
    private Integer proteins;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer fats;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer carbohydrates;

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
