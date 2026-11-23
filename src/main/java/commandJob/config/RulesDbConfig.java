package commandJob.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "commandJob.rule.repository", // где лежат наши JPA репозитории
        entityManagerFactoryRef = "rulesEntityManagerFactory",
        transactionManagerRef = "rulesTransactionManager"
)
public class RulesDbConfig {

    @Bean
    @ConfigurationProperties("spring.second-datasource")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "rulesDataSource")
    public DataSource rulesDataSource() {
        return secondDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "rulesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean rulesEntityManagerFactory(
            @Qualifier("rulesDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("commandJob.rule.entity"); // где лежат сущности
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(name = "rulesTransactionManager")
    public JpaTransactionManager rulesTransactionManager(
            @Qualifier("rulesEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(emf.getObject());
        return tm;
    }

    /// нужно явно указать Liquibase, что он должен работать со второй базой (rulesDataSource), а не с H2.
    /// так как liquibase не понял с какой базой работать
    @Bean
    public SpringLiquibase liquibase(@Qualifier("rulesDataSource") DataSource rulesDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(rulesDataSource);
        // Укажите путь к вашему файлу миграций
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        // Если нужно, можно задать другие параметры (контексты, схему и т.д.)
        // liquibase.setDefaultSchema("public");
        return liquibase;
    }

}