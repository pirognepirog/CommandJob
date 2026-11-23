package commandJob.rule.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class QueryCache {

    // Кеш для USER_OF и ACTIVE_USER_OF
    public final Cache<String, Integer> userCountCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))
            .maximumSize(10_000)
            .build();

    // Кеш для TRANSACTION_SUM_COMPARE
    public final Cache<String, Long> sumCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))
            .maximumSize(10_000)
            .build();

    // Кеш для TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
    // Храним массив из двух сумм: [deposit, withdraw]
    public final Cache<String, long[]> compareCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))
            .maximumSize(10_000)
            .build();

    public static String key(UUID userId, String... args) {
        return userId + ":" + String.join("|", args);
    }
}