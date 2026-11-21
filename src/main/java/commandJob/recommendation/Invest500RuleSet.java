package commandJob.recommendation;

import commandJob.dto.RecommendationDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final JdbcTemplate jdbc;

    public Invest500RuleSet(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        String sql = """
            SELECT
                SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS deposit_sum,
                SUM(CASE WHEN t.TYPE = 'WITHDRAW' AND p.TYPE = 'INVEST' THEN t.AMOUNT ELSE 0 END) AS invest_withdraw_sum
            FROM transactions t
            JOIN products p ON p.ID = t.PRODUCT_ID
            JOIN users u ON u.ID = t.USER_ID
            WHERE t.USER_ID = ?
            """;

        return jdbc.query(sql, rs -> {
            if (!rs.next()) return Optional.empty();
            double depositSum = rs.getDouble("deposit_sum");
            double investWithdrawSum = rs.getDouble("invest_withdraw_sum");
            if (depositSum >= 500 && investWithdrawSum == 0) {
                return Optional.of(new RecommendationDto(ID,
                        "Invest 500",
                        "Откройте инвестиционный счёт и начните инвестировать"));
            }
            return Optional.empty();
        }, userId);
    }
}