package com.nutriflow.service;

import com.nutriflow.dto.request.LogMealRequest;
import com.nutriflow.dto.response.MealLogItemResponse;
import com.nutriflow.dto.response.MealLogResponse;
import com.nutriflow.dto.response.PageResponse;
import com.nutriflow.entity.*;
import com.nutriflow.exception.ResourceNotFoundException;
import com.nutriflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealLogRepository mealLogRepository;
    private final MealCellRepository mealCellRepository;
    private final WeekMenuRepository weekMenuRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MealLogResponse> getDailyLogs(Long userId, LocalDate date) {
        return mealLogRepository.findByUserIdAndMealDateOrderByMealIndex(userId, date)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<MealLogResponse> getLogHistory(Long userId, int page, int size) {
        Page<MealLog> pageData = mealLogRepository.findByUserIdOrderByLoggedAtDesc(
                userId, PageRequest.of(page, size));
        return PageResponse.<MealLogResponse>builder()
                .content(pageData.getContent().stream().map(this::toResponse).collect(Collectors.toList()))
                .page(page).size(size)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .last(pageData.isLast())
                .build();
    }

    @Caching(evict = {
        @CacheEvict(value = "dailyNutrition", key = "#userId + ':' + #req.mealDate"),
        @CacheEvict(value = "weekMenu", allEntries = true)
    })
    @Transactional
    public MealLogResponse logMeal(Long userId, LogMealRequest req) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        MealLog log = MealLog.builder()
                .user(user)
                .mealDate(req.getMealDate())
                .mealIndex(req.getMealIndex())
                .mealType(req.getMealType() != null ? req.getMealType() : "外食")
                .storeName(req.getStoreName())
                .note(req.getNote())
                .items(new ArrayList<>())
                .build();

        if (req.getItems() != null) {
            for (int i = 0; i < req.getItems().size(); i++) {
                var dto = req.getItems().get(i);
                MealLogItem item = MealLogItem.builder()
                        .mealLog(log)
                        .foodName(dto.getFoodName())
                        .kcal(dto.getKcal())
                        .kcalRangeMin(dto.getKcalRangeMin())
                        .kcalRangeMax(dto.getKcalRangeMax())
                        .protein(dto.getProtein())
                        .carbs(dto.getCarbs())
                        .fat(dto.getFat())
                        .sodium(dto.getSodium())
                        .dataSource(dto.getDataSource())
                        .confidence(dto.getConfidence())
                        .photoUsed(dto.getPhotoUsed())
                        .referenceType(dto.getReferenceType())
                        .sortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : i)
                        .build();
                log.getItems().add(item);
            }
        }
        mealLogRepository.save(log);

        // Auto-sync to weekly menu cell
        syncToWeeklyCell(user, req, log);

        return toResponse(log);
    }

    private void syncToWeeklyCell(User user, LogMealRequest req, MealLog log) {
        LocalDate weekStart = req.getMealDate().with(DayOfWeek.MONDAY);
        int dayIndex = req.getMealDate().getDayOfWeek().getValue() - 1; // 0=Mon..6=Sun

        WeekMenu weekMenu = weekMenuRepository.findByUserIdAndWeekStart(user.getId(), weekStart)
                .orElseGet(() -> weekMenuRepository.save(
                        WeekMenu.builder()
                                .user(user).weekStart(weekStart).generatedBy("manual")
                                .cells(new ArrayList<>()).build()));

        MealCell cell = mealCellRepository
                .findByWeekMenuIdAndDayIndexAndMealIndex(weekMenu.getId(), dayIndex, req.getMealIndex())
                .orElseGet(() -> {
                    MealCell c = MealCell.builder()
                            .weekMenu(weekMenu).dayIndex(dayIndex).mealIndex(req.getMealIndex()).build();
                    return mealCellRepository.save(c);
                });

        // Append items from this log to the cell
        if (req.getItems() != null) {
            int baseOrder = cell.getItems().size();
            for (int i = 0; i < log.getItems().size(); i++) {
                MealLogItem src = log.getItems().get(i);
                cell.getItems().add(MealItem.builder()
                        .mealCell(cell)
                        .foodName(src.getFoodName())
                        .kcal(src.getKcal())
                        .protein(src.getProtein())
                        .carbs(src.getCarbs())
                        .fat(src.getFat())
                        .sodium(src.getSodium())
                        .dataSource(src.getDataSource())
                        .sortOrder(baseOrder + i)
                        .build());
            }
        }
        cell.setLogged(true);
        if (req.getMealType() != null && !req.getMealType().isEmpty()) {
            cell.setMealType(req.getMealType());
        }
        log.setMealCell(cell);
        mealCellRepository.save(cell);
    }

    @Caching(evict = {
        @CacheEvict(value = "dailyNutrition", allEntries = true),
        @CacheEvict(value = "weekMenu", allEntries = true)
    })
    @Transactional
    public void deleteLog(Long userId, Long logId) {
        MealLog log = mealLogRepository.findByIdAndUserId(logId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal log not found"));
        mealLogRepository.delete(log);
    }

    private MealLogResponse toResponse(MealLog log) {
        List<MealLogItemResponse> items = log.getItems().stream()
                .map(i -> MealLogItemResponse.builder()
                        .id(i.getId()).foodName(i.getFoodName()).kcal(i.getKcal())
                        .kcalRangeMin(i.getKcalRangeMin()).kcalRangeMax(i.getKcalRangeMax())
                        .protein(i.getProtein()).carbs(i.getCarbs()).fat(i.getFat())
                        .sodium(i.getSodium()).dataSource(i.getDataSource())
                        .confidence(i.getConfidence()).photoUsed(i.getPhotoUsed())
                        .referenceType(i.getReferenceType()).sortOrder(i.getSortOrder()).build())
                .collect(Collectors.toList());
        double total = items.stream()
                .mapToDouble(r -> r.getKcal() != null ? r.getKcal().doubleValue() : 0).sum();
        return MealLogResponse.builder()
                .id(log.getId()).mealDate(log.getMealDate()).mealIndex(log.getMealIndex())
                .mealType(log.getMealType())
                .mealCellId(log.getMealCell() != null ? log.getMealCell().getId() : null)
                .storeName(log.getStoreName()).note(log.getNote()).loggedAt(log.getLoggedAt())
                .items(items).totalKcal(total).build();
    }
}
