package ru.star.recommendation_service.rules;

import org.springframework.stereotype.Component;
import ru.star.recommendation_service.dto.RecommendationDto;
import ru.star.recommendation_service.repository.RecommendationRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private static final String PRODUCT_ID = "59efc529-2fff-41af-baff-90ccd7402925";
    private static final BigDecimal MIN_DEPOSIT = BigDecimal.valueOf(50000);

    private final RecommendationRepository repository;

    public TopSavingRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasDebit = repository.userHasProductOfType(userId, "DEBIT");

        BigDecimal debitDeposit = repository.getDepositSum(userId, "DEBIT");
        BigDecimal savingDeposit = repository.getDepositSum(userId, "SAVING");
        BigDecimal debitWithdraw = repository.getWithdrawSum(userId, "DEBIT");

        boolean enoughDeposit =
                debitDeposit.compareTo(MIN_DEPOSIT) >= 0
                        || savingDeposit.compareTo(MIN_DEPOSIT) >= 0;

        boolean depositsMoreThanWithdraws =
                debitDeposit.compareTo(debitWithdraw) > 0;

        if (hasDebit && enoughDeposit && depositsMoreThanWithdraws) {
            return Optional.of(new RecommendationDto(
                    PRODUCT_ID,
                    "Top Saving",
                    "Откройте свою собственную «Копилку» с нашим банком!"
            ));
        }

        return Optional.empty();
    }
}