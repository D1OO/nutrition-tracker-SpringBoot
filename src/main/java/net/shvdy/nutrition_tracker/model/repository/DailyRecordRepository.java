package net.shvdy.nutrition_tracker.model.repository;

import net.shvdy.nutrition_tracker.model.entity.DailyRecord;
import net.shvdy.nutrition_tracker.model.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {
    List<DailyRecord> findByUserProfileAndRecordDateBetween
            (UserProfile userProfile, String periodStartDate, String periodEndDate);

    Optional<DailyRecord> findByRecordDateAndUserProfile(String recordDate, UserProfile userProfile);
}
