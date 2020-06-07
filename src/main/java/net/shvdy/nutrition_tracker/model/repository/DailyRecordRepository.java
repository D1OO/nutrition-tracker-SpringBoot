package net.shvdy.nutrition_tracker.model.repository;

import net.shvdy.nutrition_tracker.model.entity.DailyRecord;
import net.shvdy.nutrition_tracker.model.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {
    Page<DailyRecord> findByUserProfileAndRecordDateBetween
            (UserProfile userProfile, String periodStartDate, String periodEndDate, Pageable pageable);
}
