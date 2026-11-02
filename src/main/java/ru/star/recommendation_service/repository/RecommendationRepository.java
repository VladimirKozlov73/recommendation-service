package ru.star.recommendation_service.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userHasProductOfType(UUID userId, String productType) {
        Integer count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM transactions t
                JOIN products p ON p.id = t.product_id
                WHERE t.user_id = ?
                  AND p.type = ?
                """,
                Integer.class,
                userId,
                productType
        );

        return count != null && count > 0;
    }

    public BigDecimal getDepositSum(UUID userId, String productType) {
        return getTransactionSum(userId, productType, "DEPOSIT");
    }

    public BigDecimal getWithdrawSum(UUID userId, String productType) {
        return getTransactionSum(userId, productType, "WITHDRAW");
    }

    private BigDecimal getTransactionSum(UUID userId, String productType, String transactionType) {
        BigDecimal sum = jdbcTemplate.queryForObject(
                """
                SELECT COALESCE(SUM(t.amount), 0)
                FROM transactions t
                JOIN products p ON p.id = t.product_id
                WHERE t.user_id = ?
                  AND p.type = ?
                  AND t.type = ?
                """,
                BigDecimal.class,
                userId,
                productType,
                transactionType
        );

        return sum == null ? BigDecimal.ZERO : sum;
    }
}