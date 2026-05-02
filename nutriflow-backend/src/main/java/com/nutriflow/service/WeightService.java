package com.nutriflow.service;

import com.nutriflow.dto.request.LogWeightRequest;
import com.nutriflow.dto.response.WeightRecordResponse;
import com.nutriflow.entity.User;
import com.nutriflow.entity.WeightRecord;
import com.nutriflow.exception.ResourceNotFoundException;
import com.nutriflow.repository.UserRepository;
import com.nutriflow.repository.WeightRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeightService {

    private final WeightRecordRepository weightRecordRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<WeightRecordResponse> getHistory(Long userId, int limit) {
        return weightRecordRepository.findByUserIdOrderByRecordDateDesc(userId, PageRequest.of(0, limit))
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public WeightRecordResponse logWeight(Long userId, LogWeightRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        WeightRecord record = weightRecordRepository.findByUserIdAndRecordDate(userId, req.getRecordDate())
                .orElse(WeightRecord.builder().user(user).build());
        record.setWeight(req.getWeight());
        record.setRecordDate(req.getRecordDate());
        record.setNote(req.getNote());
        weightRecordRepository.save(record);

        user.setWeight(req.getWeight());
        userRepository.save(user);

        return toResponse(record);
    }

    @Transactional
    public void deleteRecord(Long userId, Long recordId) {
        WeightRecord record = weightRecordRepository.findById(recordId)
                .filter(r -> r.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Weight record not found"));
        weightRecordRepository.delete(record);
    }

    private WeightRecordResponse toResponse(WeightRecord r) {
        return WeightRecordResponse.builder()
                .id(r.getId()).weight(r.getWeight()).recordDate(r.getRecordDate()).note(r.getNote()).build();
    }
}
