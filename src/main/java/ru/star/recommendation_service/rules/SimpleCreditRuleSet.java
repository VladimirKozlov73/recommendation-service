package ru.star.recommendation_service.rules;

import org.springframework.stereotype.Component;
import ru.star.recommendation_service.dto.RecommendationDto;
import ru.star.recommendation_service.repository.RecommendationRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private static final String PRODUCT_ID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private static final BigDecimal MIN_DEBIT_WITHDRAW = BigDecimal.valueOf(100000);

    private final RecommendationRepository repository;

    public SimpleCreditRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasCredit = repository.userHasProductOfType(userId, "CREDIT");

        BigDecimal debitDeposit = repository.getDepositSum(userId, "DEBIT");
        BigDecimal debitWithdraw = repository.getWithdrawSum(userId, "DEBIT");

        boolean depositsMoreThanWithdraws =
                debitDeposit.compareTo(debitWithdraw) > 0;

        boolean withdrawMoreThan100000 =
                debitWithdraw.compareTo(MIN_DEBIT_WITHDRAW) > 0;

        if (!hasCredit && depositsMoreThanWithdraws && withdrawMoreThan100000) {
            return Optional.of(new RecommendationDto(
                    PRODUCT_ID,
                    "Простой кредит",
                    "Откройте мир выгодных кредитов с нами!"
            ));
        }

        return Optional.empty();
    }
}