package ru.star.recommendation_service.rules;

import ru.star.recommendation_service.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {

    Optional<RecommendationDto> check(UUID userId);
}