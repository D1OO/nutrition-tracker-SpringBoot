package net.shvdy.sbproject.repository;

import net.shvdy.sbproject.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
