package commandJob.recommendation;

import commandJob.dto.RecommendationDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private static final UUID ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private final JdbcTemplate jdbc;

    public SimpleCreditRuleSet(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        String sql = """
            SELECT
                SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS deposit_sum,
                SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS withdraw_sum,
                SUM(CASE WHEN p.TYPE = 'CREDIT' THEN 1 ELSE 0 END) AS credit_count
            FROM transactions t
            JOIN products p ON p.ID = t.PRODUCT_ID
            JOIN users u ON u.ID = t.USER_ID
            WHERE t.USER_ID = ?
            """;

        return jdbc.query(sql, rs -> {
            if (!rs.next()) return Optional.empty();
            double depositSum = rs.getDouble("deposit_sum");
            double withdrawSum = rs.getDouble("withdraw_sum");
            int creditCount = rs.getInt("credit_count");
            if (creditCount == 0 && depositSum >= 100_000 && withdrawSum >= 100_000) {
                return Optional.of(new RecommendationDto(ID,
                        "Простой кредит",
                        "Оформите простой кредит на выгодных условиях"));
            }
            return Optional.empty();
        }, userId);
    }
}