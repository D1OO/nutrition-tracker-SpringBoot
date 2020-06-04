package net.shvdy.nutrition_tracker.entity;

import lombok.*;
import net.shvdy.nutrition_tracker.service.exception.NoValidProfileDataProvidedException;

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
@Table(name = "user_profile")
public class UserProfile {
    @MapsId
    @OneToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    User user;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userProfile")
    List<DailyRecord> dailyRecord;
    @Id
    @Column(name = "profile_id")
    private Long profileId;
    @Column(name = "first_name_ua")
    private String firstNameUa;
    @Column(name = "last_name")
    @NotNull
    private String lastName;
    @Column(name = "first_name")
    @NotNull
    private String firstName;
    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;
    private int age;
    private int height;
    private int weight;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userProfile", cascade = CascadeType.ALL)
    List<Food> userFood = new ArrayList<>();

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

    public int getDailyCalsNorm() throws NoValidProfileDataProvidedException {
        if (this.lifestyle == null)
            throw new NoValidProfileDataProvidedException();
        int i = (int) ((66 + 13.75 * weight + 5 * height - 6.755 * age) * lifestyle.getFactor());
        if (i <= 0)
            throw new NoValidProfileDataProvidedException();
        else return i;
    }
}