package commandJob.recommendation;

import commandJob.dto.RecommendationDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private static final UUID ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private final JdbcTemplate jdbc;

    public TopSavingRuleSet(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        String sql = """
            SELECT
                SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS deposit_sum,
                SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS withdraw_sum
            FROM transactions t
            JOIN products p ON p.ID = t.PRODUCT_ID
            JOIN users u ON u.ID = t.USER_ID
            WHERE t.USER_ID = ?
              AND p.TYPE = 'DEBIT'
            """;

        return jdbc.query(sql, rs -> {
            if (!rs.next()) return Optional.empty();
            double depositSum = rs.getDouble("deposit_sum");
            double withdrawSum = rs.getDouble("withdraw_sum");
            if (depositSum >= 50_000 && withdrawSum >= 50_000) {
                return Optional.of(new RecommendationDto(ID,
                        "Top Saving",
                        "Откройте накопительный счёт с повышенной ставкой"));
            }
            return Optional.empty();
        }, userId);
    }
}