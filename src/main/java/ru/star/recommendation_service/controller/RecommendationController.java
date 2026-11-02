package ru.star.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.star.recommendation_service.dto.UserRecommendationsDto;
import ru.star.recommendation_service.service.RecommendationService;

import java.util.UUID;

@Tag(
        name = "Recommendations",
        description = "Персональные рекомендации банковских продуктов"
)
@RestController
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Operation(
            summary = "Получить рекомендации пользователя",
            description = "Возвращает список рекомендаций для пользователя по его UUID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Рекомендации получены"),
            @ApiResponse(responseCode = "400", description = "Некорректный user_id")
    })
    @GetMapping("/recommendation/{userId}")
    public UserRecommendationsDto getRecommendations(
            @Parameter(description = "UUID пользователя", required = true)
            @PathVariable UUID userId
    ) {
        return recommendationService.getRecommendations(userId);
    }
}