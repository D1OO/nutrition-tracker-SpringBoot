package net.shvdy.nutrition_tracker.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 20.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyRecordEntryDTO {
    private Long recordId;
    private String foodName;
    private String foodDTOJSON;

    @NotNull
    @Min(1)
    @Max(5000)
    private Integer quantity;
    private FoodDTO food;

}