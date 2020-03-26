package net.shvdy.sbproject.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;
    @Column(name = "first_name_ua")
    private String firstNameUa;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;
    private int age;
    private int height;
    private int weight;
    @Column(name = "first_name")
    @NotNull
    private String firstName;

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

    public int getDailyCalsNorm() {
        try {
            return (int) ((66 + 13.75 * weight + 5 * height - 6.755 * age) * lifestyle.getFactor());
        } catch (NullPointerException e) {
            return -1;
        }
    }
}