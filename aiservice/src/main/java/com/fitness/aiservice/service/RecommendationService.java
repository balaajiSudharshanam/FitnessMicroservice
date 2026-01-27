package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationsRespository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationsRespository recommendationsRespository;

    public  List<Recommendation> getUserRecommendation(String userId) {
        return recommendationsRespository.findByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recommendationsRespository.findByActivityId(activityId).orElseThrow(()-> new RuntimeException("no recommendation found for this activity"));
    }
}
