package net.shvdy.nutrition_tracker.model.repository;

import net.shvdy.nutrition_tracker.model.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
