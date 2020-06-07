package net.shvdy.nutrition_tracker.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

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
@Table(name = "daily_record")
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "record_id")
    private Long recordId;

    @NotNull
    @Column(name = "record_date")
    private String recordDate;

    @NotNull
    @Column(name = "daily_calories_norm")
    private int dailyCaloriesNorm;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    UserProfile userProfile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dailyRecord", cascade = CascadeType.ALL)
    private List<DailyRecordEntry> entries;
}
