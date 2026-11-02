package ru.star.recommendation_service.service;

import org.springframework.stereotype.Service;
import ru.star.recommendation_service.dto.RecommendationDto;
import ru.star.recommendation_service.dto.UserRecommendationsDto;
import ru.star.recommendation_service.rules.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> rules;

    public RecommendationService(List<RecommendationRuleSet> rules) {
        this.rules = rules;
    }

    public UserRecommendationsDto getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations = rules.stream()
                .map(rule -> rule.check(userId))
                .flatMap(Optional::stream)
                .toList();

        return new UserRecommendationsDto(userId.toString(), recommendations);
    }
}