package com.nutriflow.repository;

import com.nutriflow.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUserId(Long userId);
    Optional<UserAchievement> findByUserIdAndAchievementId(Long userId, Long achievementId);

    @Query("SELECT COUNT(ua) > 0 FROM UserAchievement ua WHERE ua.user.id = :userId AND ua.achievement.code = :code")
    boolean existsByUserIdAndAchievementCode(@Param("userId") Long userId, @Param("code") String code);
}
