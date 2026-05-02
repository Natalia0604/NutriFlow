package com.nutriflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutriflow.dto.request.CreateBlankMenuRequest;
import com.nutriflow.dto.request.MenuGenerateRequest;
import com.nutriflow.dto.request.UpdateMealCellRequest;
import com.nutriflow.dto.response.MealCellResponse;
import com.nutriflow.dto.response.MealItemResponse;
import com.nutriflow.dto.response.WeekMenuResponse;
import com.nutriflow.entity.*;
import com.nutriflow.exception.AiServiceException;
import com.nutriflow.exception.ResourceNotFoundException;
import com.nutriflow.repository.*;
import com.nutriflow.util.TDEECalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final WeekMenuRepository weekMenuRepository;
    private final MealCellRepository mealCellRepository;
    private final UserRepository userRepository;
    private final FoodPreferenceRepository foodPreferenceRepository;
    private final AiProxyService aiProxyService;
    private final TDEECalculator tdeeCalculator;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "weekMenu", key = "#userId + ':' + #weekStart", unless = "#result == null")
    public WeekMenuResponse getWeekMenu(Long userId, LocalDate weekStart) {
        return weekMenuRepository.findByUserIdAndWeekStart(userId, weekStart)
                .map(this::toResponse)
                .orElse(null);
    }

    @CacheEvict(value = "weekMenu", key = "#userId + ':' + #req.weekStart")
    @Transactional
    public WeekMenuResponse generateMenu(Long userId, MenuGenerateRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!req.getForceRegenerate()) {
            return weekMenuRepository.findByUserIdAndWeekStart(userId, req.getWeekStart())
                    .map(this::toResponse)
                    .orElseGet(() -> createAiMenu(user, req));
        }
        weekMenuRepository.findByUserIdAndWeekStart(userId, req.getWeekStart())
                .ifPresent(weekMenuRepository::delete);
        return createAiMenu(user, req);
    }

    @CacheEvict(value = "weekMenu", allEntries = true)
    @Transactional
    public WeekMenuResponse createBlankMenu(Long userId, CreateBlankMenuRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return weekMenuRepository.findByUserIdAndWeekStart(userId, req.getWeekStart())
                .map(this::toResponse)
                .orElseGet(() -> {
                    WeekMenu menu = WeekMenu.builder()
                            .user(user)
                            .weekStart(req.getWeekStart())
                            .generatedBy("manual")
                            .cells(new ArrayList<>())
                            .build();
                    weekMenuRepository.save(menu);
                    return toResponse(menu);
                });
    }

    @CacheEvict(value = "weekMenu", allEntries = true)
    @Transactional
    public MealCellResponse updateMealCell(Long userId, Long menuId, UpdateMealCellRequest req) {
        WeekMenu menu = weekMenuRepository.findById(menuId)
                .filter(m -> m.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        MealCell cell = mealCellRepository
                .findByWeekMenuIdAndDayIndexAndMealIndex(menuId, req.getDayIndex(), req.getMealIndex())
                .orElse(MealCell.builder().weekMenu(menu).dayIndex(req.getDayIndex()).mealIndex(req.getMealIndex()).build());

        if (req.getMealType() != null) cell.setMealType(req.getMealType());
        if (req.getLogTime() != null) cell.setLogTime(req.getLogTime());
        if (req.getNote() != null) cell.setNote(req.getNote());

        if (req.getItems() != null) {
            cell.getItems().clear();
            for (int i = 0; i < req.getItems().size(); i++) {
                var itemDto = req.getItems().get(i);
                MealItem item = MealItem.builder()
                        .mealCell(cell)
                        .foodName(itemDto.getFoodName())
                        .kcal(itemDto.getKcal())
                        .protein(itemDto.getProtein())
                        .carbs(itemDto.getCarbs())
                        .fat(itemDto.getFat())
                        .sodium(itemDto.getSodium())
                        .dataSource(itemDto.getDataSource())
                        .sortOrder(itemDto.getSortOrder() != null ? itemDto.getSortOrder() : i)
                        .build();
                cell.getItems().add(item);
            }
        }
        mealCellRepository.save(cell);
        return toCellResponse(cell);
    }

    private WeekMenuResponse createAiMenu(User user, MenuGenerateRequest req) {
        double targetKcal = tdeeCalculator.targetCalories(user);
        List<String> preferences = foodPreferenceRepository.findByUserId(user.getId())
                .stream().map(FoodPreference::getFoodName).collect(Collectors.toList());

        String systemPrompt = """
                你是一位專業的台灣飲食規劃師，請根據用戶的健康目標與飲食偏好，
                生成一週7天（週一到週日）共21餐的飲食計畫（dayIndex 0=週一，mealIndex 0=早餐/1=午餐/2=晚餐）。
                回應格式為 JSON，結構為：
                {
                  "comment": "整體說明",
                  "cells": [
                    {"dayIndex":0,"mealIndex":0,"mealType":"早餐","note":"...",
                     "items":[{"foodName":"食物名","kcal":300.0,"protein":10.0,"carbs":40.0,"fat":8.0,"sodium":200.0,"dataSource":"ai"}]}
                  ]
                }
                每日總熱量目標約 %d 大卡。只回傳 JSON，不要其他文字。
                """.formatted((int) targetKcal);

        String userMessage = "偏好食物：" + String.join("、", preferences)
                + "\n週起始日：" + req.getWeekStart()
                + (req.getExtraNote() != null ? "\n備注：" + req.getExtraNote() : "");

        String aiJson;
        try {
            aiJson = aiProxyService.chat(systemPrompt, userMessage);
        } catch (Exception e) {
            throw new AiServiceException("菜單生成失敗", e);
        }

        return parseAndSaveMenu(user, req.getWeekStart(), aiJson);
    }

    private WeekMenuResponse parseAndSaveMenu(User user, LocalDate weekStart, String aiJson) {
        try {
            JsonNode root = objectMapper.readTree(aiJson);
            WeekMenu menu = WeekMenu.builder()
                    .user(user)
                    .weekStart(weekStart)
                    .aiComment(root.path("comment").asText(""))
                    .generatedBy("ai")
                    .cells(new ArrayList<>())
                    .build();

            for (JsonNode cellNode : root.path("cells")) {
                MealCell cell = MealCell.builder()
                        .weekMenu(menu)
                        .dayIndex(cellNode.path("dayIndex").asInt())
                        .mealIndex(cellNode.path("mealIndex").asInt())
                        .mealType(cellNode.path("mealType").asText(""))
                        .note(cellNode.path("note").asText(""))
                        .source("ai")
                        .logged(false)
                        .items(new ArrayList<>())
                        .build();

                int sortOrder = 0;
                for (JsonNode itemNode : cellNode.path("items")) {
                    MealItem item = MealItem.builder()
                            .mealCell(cell)
                            .foodName(itemNode.path("foodName").asText())
                            .kcal(bd(itemNode, "kcal"))
                            .protein(bd(itemNode, "protein"))
                            .carbs(bd(itemNode, "carbs"))
                            .fat(bd(itemNode, "fat"))
                            .sodium(bd(itemNode, "sodium"))
                            .dataSource(itemNode.path("dataSource").asText("ai"))
                            .sortOrder(sortOrder++)
                            .build();
                    cell.getItems().add(item);
                }
                menu.getCells().add(cell);
            }
            weekMenuRepository.save(menu);
            return toResponse(menu);
        } catch (Exception e) {
            log.error("Failed to parse AI menu JSON: {}", e.getMessage(), e);
            throw new AiServiceException("AI 回應格式解析失敗");
        }
    }

    private BigDecimal bd(JsonNode node, String field) {
        if (!node.has(field)) return BigDecimal.ZERO;
        try {
            return new BigDecimal(node.get(field).asText("0"));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private WeekMenuResponse toResponse(WeekMenu menu) {
        List<MealCellResponse> cells = menu.getCells().stream()
                .map(this::toCellResponse).collect(Collectors.toList());
        return WeekMenuResponse.builder()
                .id(menu.getId())
                .weekStart(menu.getWeekStart())
                .aiComment(menu.getAiComment())
                .generatedBy(menu.getGeneratedBy())
                .cells(cells)
                .build();
    }

    private MealCellResponse toCellResponse(MealCell cell) {
        List<MealItemResponse> items = cell.getItems().stream()
                .map(i -> MealItemResponse.builder()
                        .id(i.getId()).foodName(i.getFoodName()).kcal(i.getKcal())
                        .protein(i.getProtein()).carbs(i.getCarbs()).fat(i.getFat())
                        .sodium(i.getSodium()).dataSource(i.getDataSource())
                        .confidence(i.getConfidence()).sortOrder(i.getSortOrder()).build())
                .collect(Collectors.toList());
        double total = items.stream()
                .mapToDouble(r -> r.getKcal() != null ? r.getKcal().doubleValue() : 0).sum();
        return MealCellResponse.builder()
                .id(cell.getId()).dayIndex(cell.getDayIndex()).mealIndex(cell.getMealIndex())
                .mealType(cell.getMealType()).logged(cell.getLogged()).logTime(cell.getLogTime())
                .note(cell.getNote()).source(cell.getSource()).items(items).totalKcal(total).build();
    }
}
