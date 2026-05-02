package com.nutriflow.service;

import com.nutriflow.client.OpenFoodFactsClient;
import com.nutriflow.dto.request.AddFoodPreferenceRequest;
import com.nutriflow.dto.request.SaveCustomFoodRequest;
import com.nutriflow.dto.response.CustomFoodResponse;
import com.nutriflow.dto.response.FoodPreferenceResponse;
import com.nutriflow.dto.response.FoodSearchResponse;
import com.nutriflow.entity.CustomFood;
import com.nutriflow.entity.FoodPreference;
import com.nutriflow.entity.OfficialNutrition;
import com.nutriflow.exception.ResourceNotFoundException;
import com.nutriflow.repository.CustomFoodRepository;
import com.nutriflow.repository.FoodPreferenceRepository;
import com.nutriflow.repository.OfficialNutritionRepository;
import com.nutriflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodPreferenceRepository foodPreferenceRepository;
    private final OfficialNutritionRepository officialNutritionRepository;
    private final CustomFoodRepository customFoodRepository;
    private final OpenFoodFactsClient openFoodFactsClient;
    private final UserRepository userRepository;

    // ── Search ──────────────────────────────────────────────────────────────

    public List<FoodSearchResponse> search(String query, String store, Long userId) {
        List<FoodSearchResponse> results = new ArrayList<>();

        // 1. User's personal food library (exact prefix match)
        if (userId != null) {
            customFoodRepository.findByUserIdAndFoodNameContainingIgnoreCase(userId, query)
                    .stream().limit(5)
                    .forEach(c -> results.add(toCustomFoodSearchResponse(c)));
        }

        // 2. Official nutrition DB (full-text)
        List<OfficialNutrition> official = officialNutritionRepository.fullTextSearch(query + "*", 10);
        official.forEach(o -> results.add(FoodSearchResponse.builder()
                .id(o.getId()).storeName(o.getStoreName()).itemName(o.getItemName())
                .sizeLabel(o.getSizeLabel()).kcal(o.getKcal()).protein(o.getProtein())
                .carbs(o.getCarbs()).fat(o.getFat()).sodium(o.getSodium()).sugar(o.getSugar())
                .dataUrl(o.getDataUrl()).source("official").build()));

        // 3. OpenFoodFacts fallback
        if (results.size() < 5) {
            results.addAll(openFoodFactsClient.search(query, store));
        }
        return results;
    }

    private FoodSearchResponse toCustomFoodSearchResponse(CustomFood c) {
        return FoodSearchResponse.builder()
                .id(c.getId())
                .itemName(c.getFoodName())
                .sizeLabel(c.getSizeLabel())
                .kcal(c.getKcal())
                .protein(c.getProtein())
                .carbs(c.getCarbs())
                .fat(c.getFat())
                .sodium(c.getSodium())
                .source("custom")
                .build();
    }

    // ── Custom food CRUD ─────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<CustomFoodResponse> getCustomFoods(Long userId) {
        return customFoodRepository.findByUserId(userId)
                .stream().map(this::toCustomFoodResponse).collect(Collectors.toList());
    }

    @Transactional
    public CustomFoodResponse createCustomFood(Long userId, SaveCustomFoodRequest req) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CustomFood food = CustomFood.builder()
                .user(user)
                .foodName(req.getFoodName())
                .sizeLabel(req.getSizeLabel())
                .kcal(req.getKcal())
                .protein(req.getProtein())
                .carbs(req.getCarbs())
                .fat(req.getFat())
                .sodium(req.getSodium())
                .note(req.getNote())
                .build();
        customFoodRepository.save(food);
        return toCustomFoodResponse(food);
    }

    @Transactional
    public CustomFoodResponse updateCustomFood(Long userId, Long foodId, SaveCustomFoodRequest req) {
        CustomFood food = customFoodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Custom food not found"));
        food.setFoodName(req.getFoodName());
        food.setSizeLabel(req.getSizeLabel());
        food.setKcal(req.getKcal());
        food.setProtein(req.getProtein());
        food.setCarbs(req.getCarbs());
        food.setFat(req.getFat());
        food.setSodium(req.getSodium());
        food.setNote(req.getNote());
        customFoodRepository.save(food);
        return toCustomFoodResponse(food);
    }

    @Transactional
    public void deleteCustomFood(Long userId, Long foodId) {
        customFoodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Custom food not found"));
        customFoodRepository.deleteByIdAndUserId(foodId, userId);
    }

    private CustomFoodResponse toCustomFoodResponse(CustomFood c) {
        return CustomFoodResponse.builder()
                .id(c.getId())
                .foodName(c.getFoodName())
                .sizeLabel(c.getSizeLabel())
                .kcal(c.getKcal())
                .protein(c.getProtein())
                .carbs(c.getCarbs())
                .fat(c.getFat())
                .sodium(c.getSodium())
                .note(c.getNote())
                .createdAt(c.getCreatedAt())
                .build();
    }

    // ── Food preferences ─────────────────────────────────────────────────────

    public List<FoodPreferenceResponse> getPreferences(Long userId) {
        return foodPreferenceRepository.findByUserId(userId).stream()
                .map(this::toPrefResponse).collect(Collectors.toList());
    }

    @Transactional
    public FoodPreferenceResponse addPreference(Long userId, AddFoodPreferenceRequest req) {
        // Idempotent: return existing entry if same (user, foodName) already present
        return foodPreferenceRepository.findByUserIdAndFoodName(userId, req.getFoodName())
                .map(this::toPrefResponse)
                .orElseGet(() -> {
                    var user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    FoodPreference pref = FoodPreference.builder()
                            .user(user)
                            .foodName(req.getFoodName())
                            .category(req.getCategory())
                            .isCustom(req.getIsCustom() != null ? req.getIsCustom() : false)
                            .isAllergy(req.getIsAllergy() != null ? req.getIsAllergy() : false)
                            .build();
                    foodPreferenceRepository.save(pref);
                    return toPrefResponse(pref);
                });
    }

    @Transactional
    public List<FoodPreferenceResponse> syncPreferences(Long userId, List<AddFoodPreferenceRequest> requests) {
        // Replace all preferences for this user with the given list
        var existing = foodPreferenceRepository.findByUserId(userId);
        foodPreferenceRepository.deleteAll(existing);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<FoodPreference> prefs = requests.stream().map(req ->
                FoodPreference.builder()
                        .user(user)
                        .foodName(req.getFoodName())
                        .category(req.getCategory())
                        .isCustom(req.getIsCustom() != null ? req.getIsCustom() : false)
                        .isAllergy(req.getIsAllergy() != null ? req.getIsAllergy() : false)
                        .build()
        ).collect(Collectors.toList());
        foodPreferenceRepository.saveAll(prefs);
        return prefs.stream().map(this::toPrefResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deletePreference(Long userId, Long prefId) {
        foodPreferenceRepository.deleteByIdAndUserId(prefId, userId);
    }

    private FoodPreferenceResponse toPrefResponse(FoodPreference p) {
        return FoodPreferenceResponse.builder()
                .id(p.getId()).foodName(p.getFoodName()).category(p.getCategory())
                .isCustom(p.getIsCustom()).isAllergy(p.getIsAllergy()).build();
    }
}
