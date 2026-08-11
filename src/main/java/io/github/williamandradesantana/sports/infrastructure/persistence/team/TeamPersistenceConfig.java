package io.github.williamandradesantana.sports.infrastructure.persistence.team;

import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamPersistenceConfig {

    @Bean
    public TeamMapper teamMapper() {
        return new TeamMapper();
    }

    @Bean
    public TeamRepository teamRepository(TeamJpaRepository jpaRepository, TeamMapper mapper) {
        return new TeamRepositoryImpl(jpaRepository, mapper);
    }
}
