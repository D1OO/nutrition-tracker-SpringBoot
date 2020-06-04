package net.shvdy.nutrition_tracker.entity;

import lombok.*;

import javax.persistence.*;
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
@Entity
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Long food_id;

    @NotNull
    private String name;
    @NotNull
    private int calories;
    @NotNull
    private int proteins;
    @NotNull
    private int fats;
    @NotNull
    private int carbohydrates;

}
