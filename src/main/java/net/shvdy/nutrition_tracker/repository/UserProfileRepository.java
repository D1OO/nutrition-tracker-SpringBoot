package net.shvdy.nutrition_tracker.repository;

import net.shvdy.nutrition_tracker.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
