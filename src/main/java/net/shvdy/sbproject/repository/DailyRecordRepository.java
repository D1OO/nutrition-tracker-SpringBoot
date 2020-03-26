package net.shvdy.sbproject.repository;

import net.shvdy.sbproject.entity.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {

    Optional<DailyRecord> findByUserIdAndRecordDate(Long user_id, String record_date);
}
