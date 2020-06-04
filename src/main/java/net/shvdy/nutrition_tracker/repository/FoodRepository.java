package net.shvdy.nutrition_tracker.repository;

import net.shvdy.nutrition_tracker.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
