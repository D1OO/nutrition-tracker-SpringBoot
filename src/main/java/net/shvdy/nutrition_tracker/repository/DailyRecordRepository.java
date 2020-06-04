package net.shvdy.nutrition_tracker.repository;

import net.shvdy.nutrition_tracker.entity.DailyRecord;
import net.shvdy.nutrition_tracker.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {
    Page<DailyRecord> findByUserProfileAndRecordDateLessThanEqualOrderByRecordDateDesc
            (UserProfile userProfile, String recordDate, Pageable pageable);
}
