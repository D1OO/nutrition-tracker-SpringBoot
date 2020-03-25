package net.shvdy.sbproject.entity;

import lombok.*;

import javax.persistence.*;
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
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;

    private int age;
    private int height;
    private int weight;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userProfile", cascade = CascadeType.ALL)
    List<Food> userFood;

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

    // If user's profile isn't filled,
    // Thymeleaf will check on '-1' value and display 'fill profile to proceed' window,
    // otherwise, if normal value is returned - user's 'food diary' will be loaded

    // 'Optional<Integer> can't be used, because Thymeleaf can't call .isPresent() or .get(),
    // nor any other method except Thymeleaf's '#...' predefined expressions or variable's class methods like this one
    public int getDailyCalsNorm() {
        try {
            return (int) ((66 + 13.75 * weight + 5 * height - 6.755 * age) * lifestyle.getFactor());
        } catch (NullPointerException e) {
            return -1;
        }
    }
}