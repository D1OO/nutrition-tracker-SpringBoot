package net.shvdy.sbproject.entity;

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
@Table(name = "user_food")
public class Food {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    UserProfile userProfile;
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

//    @OneToOne
//    @JoinColumn(name="food_id")
//    private DailyRecordEntry dailyRecordEntry;

}
