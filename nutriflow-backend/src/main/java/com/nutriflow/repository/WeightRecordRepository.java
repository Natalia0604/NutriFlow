package com.nutriflow.repository;

import com.nutriflow.entity.WeightRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
    List<WeightRecord> findByUserIdOrderByRecordDateDesc(Long userId, Pageable pageable);
    Optional<WeightRecord> findByUserIdAndRecordDate(Long userId, LocalDate recordDate);
}
