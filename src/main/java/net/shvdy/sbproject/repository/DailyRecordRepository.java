package net.shvdy.sbproject.repository;

import net.shvdy.sbproject.entity.DailyRecord;
import net.shvdy.sbproject.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {
    Page<DailyRecord> queryByUserProfileAndRecordDateLessThanEqualOrderByRecordDateDesc
            (UserProfile userProfile, String recordDate, Pageable pageable);
}
