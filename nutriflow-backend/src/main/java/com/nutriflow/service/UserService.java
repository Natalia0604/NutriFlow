package com.nutriflow.service;

import com.nutriflow.dto.request.UpdateCatRequest;
import com.nutriflow.dto.request.UpdateUserRequest;
import com.nutriflow.dto.response.CatProfileResponse;
import com.nutriflow.dto.response.UserProfileResponse;
import com.nutriflow.entity.CatProfile;
import com.nutriflow.entity.User;
import com.nutriflow.exception.ResourceNotFoundException;
import com.nutriflow.repository.CatProfileRepository;
import com.nutriflow.repository.UserRepository;
import com.nutriflow.util.TDEECalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CatProfileRepository catProfileRepository;
    private final TDEECalculator tdeeCalculator;

    @Cacheable(value = "userProfile", key = "#userId")
    public UserProfileResponse getProfile(Long userId) {
        User user = findUser(userId);
        double tdee = tdeeCalculator.calculate(user);
        double target = tdeeCalculator.targetCalories(user);
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .gender(user.getGender())
                .age(user.getAge())
                .weight(user.getWeight())
                .height(user.getHeight())
                .activityLevel(user.getActivityLevel())
                .hasWeightGoal(user.getHasWeightGoal())
                .targetWeight(user.getTargetWeight())
                .goalWeeks(user.getGoalWeeks())
                .isSetupComplete(user.getIsSetupComplete())
                .tdee(Math.round(tdee * 10.0) / 10.0)
                .targetCalories(Math.round(target * 10.0) / 10.0)
                .build();
    }

    @CacheEvict(value = "userProfile", key = "#userId")
    @Transactional
    public UserProfileResponse updateProfile(Long userId, UpdateUserRequest req) {
        User user = findUser(userId);
        if (req.getAge() != null) user.setAge(req.getAge());
        if (req.getWeight() != null) user.setWeight(BigDecimal.valueOf(req.getWeight()));
        if (req.getHeight() != null) user.setHeight(BigDecimal.valueOf(req.getHeight()));
        if (req.getActivityLevel() != null) user.setActivityLevel(req.getActivityLevel());
        if (req.getHasWeightGoal() != null) user.setHasWeightGoal(req.getHasWeightGoal());
        if (req.getTargetWeight() != null) user.setTargetWeight(BigDecimal.valueOf(req.getTargetWeight()));
        if (req.getGoalWeeks() != null) user.setGoalWeeks(req.getGoalWeeks());
        if (req.getIsSetupComplete() != null) user.setIsSetupComplete(req.getIsSetupComplete());
        userRepository.save(user);
        return getProfile(userId);
    }

    public CatProfileResponse getCatProfile(Long userId) {
        CatProfile cat = catProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cat profile not found"));
        return toCatResponse(cat);
    }

    @Transactional
    public CatProfileResponse updateCatProfile(Long userId, UpdateCatRequest req) {
        User user = findUser(userId);
        CatProfile cat = catProfileRepository.findByUserId(userId)
                .orElse(CatProfile.builder().user(user).build());
        cat.setBreed(req.getBreed());
        cat.setName(req.getName());
        if (req.getKnucklePxRatio() != null) cat.setKnucklePxRatio(BigDecimal.valueOf(req.getKnucklePxRatio()));
        if (req.getBowlWidthMm() != null) cat.setBowlWidthMm(BigDecimal.valueOf(req.getBowlWidthMm()));
        catProfileRepository.save(cat);
        return toCatResponse(cat);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    private CatProfileResponse toCatResponse(CatProfile cat) {
        return CatProfileResponse.builder()
                .id(cat.getId())
                .breed(cat.getBreed())
                .name(cat.getName())
                .knucklePxRatio(cat.getKnucklePxRatio())
                .bowlWidthMm(cat.getBowlWidthMm())
                .build();
    }
}
