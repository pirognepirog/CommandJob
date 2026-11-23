package commandJob.rule.engine;

import commandJob.rule.cache.QueryCache;
import commandJob.rule.model.RuleQuery;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class QueryExecutor {

    private final JdbcTemplate jdbc;
    private final QueryCache cache;

    public QueryExecutor(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbc, QueryCache cache) {
        this.jdbc = jdbc;
        this.cache = cache;
    }

    public boolean execute(UUID userId, RuleQuery q) {
        boolean result = switch (q.getQuery()) {
            case "USER_OF" -> userOf(userId, q.getArguments().get(0));
            case "ACTIVE_USER_OF" -> activeUserOf(userId, q.getArguments().get(0));
            case "TRANSACTION_SUM_COMPARE" -> transactionSumCompare(userId, q.getArguments());
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> sumCompareDepositWithdraw(userId, q.getArguments());
            default -> throw new IllegalArgumentException("Unknown query: " + q.getQuery());
        };
        return q.isNegate() ? !result : result;
    }

    private boolean userOf(UUID userId, String productType) {
        String k = QueryCache.key(userId, "USER_OF", productType);
        Integer cnt = cache.userCountCache.get(k, key -> jdbc.queryForObject("""
            SELECT COUNT(*) FROM transactions t
            JOIN products p ON p.ID = t.PRODUCT_ID
            WHERE t.USER_ID = ? AND p.TYPE = ?
            """, Integer.class, userId, productType));
        return cnt != null && cnt > 0;
    }

    private boolean activeUserOf(UUID userId, String productType) {
        String k = QueryCache.key(userId, "ACTIVE_USER_OF", productType);
        Integer cnt = cache.userCountCache.get(k, key -> jdbc.queryForObject("""
            SELECT COUNT(*) FROM transactions t
            JOIN products p ON p.ID = t.PRODUCT_ID
            WHERE t.USER_ID = ? AND p.TYPE = ?
            """, Integer.class, userId, productType));
        return cnt != null && cnt >= 5;
    }

    private boolean transactionSumCompare(UUID userId, List<String> args) {
        String productType = args.get(0);
        String txType = args.get(1);
        String op = args.get(2);
        long c = Long.parseLong(args.get(3));

        String k = QueryCache.key(userId, "SUM", productType, txType);
        Long sum = cache.sumCache.get(k, key -> jdbc.queryForObject("""
            SELECT COALESCE(SUM(t.AMOUNT),0) FROM transactions t
            JOIN products p ON p.ID = t.PRODUCT_ID
            WHERE t.USER_ID = ? AND p.TYPE = ? AND t.TYPE = ?
            """, Long.class, userId, productType, txType));
        long s = sum == null ? 0 : sum;
        return compare(s, c, op);
    }

    private boolean sumCompareDepositWithdraw(UUID userId, List<String> args) {
        String productType = args.get(0);
        String op = args.get(1);

        String k = QueryCache.key(userId, "SUM_DEP_WD", productType);
        long[] sums = cache.compareCache.get(k, key -> {
            Long dep = jdbc.queryForObject("""
                SELECT COALESCE(SUM(t.AMOUNT),0) FROM transactions t
                JOIN products p ON p.ID = t.PRODUCT_ID
                WHERE t.USER_ID = ? AND p.TYPE = ? AND t.TYPE = 'DEPOSIT'
                """, Long.class, userId, productType);
            Long wd = jdbc.queryForObject("""
                SELECT COALESCE(SUM(t.AMOUNT),0) FROM transactions t
                JOIN products p ON p.ID = t.PRODUCT_ID
                WHERE t.USER_ID = ? AND p.TYPE = ? AND t.TYPE = 'WITHDRAW'
                """, Long.class, userId, productType);
            return new long[]{dep == null ? 0 : dep, wd == null ? 0 : wd};
        });

        return compare(sums[0], sums[1], op);
    }

    private boolean compare(long a, long b, String op) {
        return switch (op) {
            case ">" -> a > b;
            case "<" -> a < b;
            case "=" -> a == b;
            case ">=" -> a >= b;
            case "<=" -> a <= b;
            default -> throw new IllegalArgumentException("Unknown op: " + op);
        };
    }
}