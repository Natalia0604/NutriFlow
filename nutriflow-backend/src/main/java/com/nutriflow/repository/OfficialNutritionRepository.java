package com.nutriflow.repository;

import com.nutriflow.entity.OfficialNutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficialNutritionRepository extends JpaRepository<OfficialNutrition, Long> {
    @Query(value = "SELECT * FROM official_nutrition WHERE MATCH(store_name, item_name) AGAINST(:query IN BOOLEAN MODE) LIMIT :limit", nativeQuery = true)
    List<OfficialNutrition> fullTextSearch(@Param("query") String query, @Param("limit") int limit);

    List<OfficialNutrition> findByStoreNameAndItemNameContainingIgnoreCase(String storeName, String itemName);
    List<OfficialNutrition> findByItemNameContainingIgnoreCaseOrderByItemName(String itemName);
}
