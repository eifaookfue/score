package com.naka.jbs.score;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableJpaRepositories(basePackages = "com.naka.jbs.score.domain.repository.score", entityManagerFactoryRef = "scoreEntityManager", transactionManagerRef = "scoreTransactionManager")
public class ScoreDataSourceConfiguration {
    @Bean(name = "scoreProperties")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.score")
    public DataSourceProperties scoreProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "scoreDataSource")
    @Primary
    public DataSource scoreDataSource(@Qualifier("scoreProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "scoreEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean scoreEntityManager(EntityManagerFactoryBuilder builder, @Qualifier("scoreDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.naka.jbs.score.domain.model.entity.score")
                .persistenceUnit("score").build();
    }

    @Bean(name = "scoreTransactionManager")
    @Primary
    public JpaTransactionManager scoreTransactionManager(@Qualifier("scoreEntityManager") LocalContainerEntityManagerFactoryBean scoreEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(scoreEntityManager.getObject());
        return transactionManager;
    }
}