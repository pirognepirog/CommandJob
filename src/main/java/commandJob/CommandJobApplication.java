package commandJob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
public class CommandJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommandJobApplication.class, args);
    }


/*    я прочитал базу данных так (код ниже), мне помог deepseek, dbreaver не как не хочет к ней подключаться
подозрения на конфликт драйверов, они были выровненны с проектом, но все равно возникает конфликт, просит
постоянно обновить драйвер, но при обновлении уходит в ошибку, я пробовал все способы подключения к H2, которые
есть в dbreaver, но результатов положительных я не получил не каких, кроме запретов на подключения
или конфликтов.
я сбросил кэш, но результатов это не дало, использовал драйвер, указанный в задании.
я пытался подключиться и при остановленном приложении, но это не дало результатов

    @Bean
    public CommandLineRunner printData(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        return args -> {
            System.out.println("\n=== ДАННЫЕ ИЗ БАЗЫ H2 ===");
            try {
                System.out.println("\n--- USERS (первые 5) ---");
                jdbcTemplate.queryForList("SELECT * FROM users LIMIT 5").forEach(System.out::println);

                System.out.println("\n--- TRANSACTIONS (первые 5) ---");
                jdbcTemplate.queryForList("SELECT * FROM transactions LIMIT 5").forEach(System.out::println);
            } catch (Exception e) {
                System.out.println("Ошибка при чтении: " + e.getMessage());
            }
            System.out.println("=========================\n");
        };
    }

 */
}
