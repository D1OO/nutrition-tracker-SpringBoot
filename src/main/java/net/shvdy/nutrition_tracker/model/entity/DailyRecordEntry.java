package net.shvdy.nutrition_tracker.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 18.03.2020
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
@Table(name = "daily_record_entry")
public class DailyRecordEntry {

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "record_id")
    private DailyRecord dailyRecord;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "entry_id")
    private Long entryId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @NotNull
    private int quantity;

}