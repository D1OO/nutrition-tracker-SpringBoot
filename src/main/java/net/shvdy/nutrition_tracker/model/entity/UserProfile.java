package net.shvdy.nutrition_tracker.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 22.03.2020
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
@Table(name = "profile")
public class UserProfile {
    @MapsId
    @OneToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    User user;

    @Id
    @Column(name = "profile_id")
    private Long profileId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "profile_to_food",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id"))
    List<Food> userFood = new ArrayList<>();

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "first_name_ua")
    private String firstNameUa;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userProfile")
    List<DailyRecord> dailyRecord;

    private int age;
    private int height;
    private int weight;

    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle = Lifestyle.MODERATELY_ACTIVE;

    public enum Lifestyle {
        SEDENTARY(1.2f),
        LIGHTLY_ACTIVE(1.375f),
        MODERATELY_ACTIVE(1.55f),
        VERY_ACTIVE(1.725f),
        EXTRA_ACTIVE(1.9f);

        float factor;

        Lifestyle(float factor) {
            this.factor = factor;
        }

        public float getFactor() {
            return factor;
        }
    }

}