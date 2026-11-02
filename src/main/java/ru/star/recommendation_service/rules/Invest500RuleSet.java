package ru.star.recommendation_service.rules;

import org.springframework.stereotype.Component;
import ru.star.recommendation_service.dto.RecommendationDto;
import ru.star.recommendation_service.repository.RecommendationRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private static final String PRODUCT_ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private static final BigDecimal MIN_SAVING_DEPOSIT = BigDecimal.valueOf(1000);

    private final RecommendationRepository repository;

    public Invest500RuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasDebit = repository.userHasProductOfType(userId, "DEBIT");
        boolean hasInvest = repository.userHasProductOfType(userId, "INVEST");
        boolean savingDepositMoreThan1000 =
                repository.getDepositSum(userId, "SAVING")
                        .compareTo(MIN_SAVING_DEPOSIT) > 0;

        if (hasDebit && !hasInvest && savingDepositMoreThan1000) {
            return Optional.of(new RecommendationDto(
                    PRODUCT_ID,
                    "Invest 500",
                    "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка!"
            ));
        }

        return Optional.empty();
    }
}