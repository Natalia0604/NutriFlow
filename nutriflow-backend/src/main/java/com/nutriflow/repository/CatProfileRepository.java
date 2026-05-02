package com.nutriflow.repository;

import com.nutriflow.entity.CatProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatProfileRepository extends JpaRepository<CatProfile, Long> {
    Optional<CatProfile> findByUserId(Long userId);
}
