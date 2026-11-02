package ru.star.recommendation_service.dto;

import java.util.List;

public record UserRecommendationsDto(
        String userId,
        List<RecommendationDto> recommendations
) {
}