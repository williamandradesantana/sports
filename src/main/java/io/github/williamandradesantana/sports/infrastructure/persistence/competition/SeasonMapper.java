package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import io.github.williamandradesantana.sports.domain.competition.Season;

public class SeasonMapper {

    public Season toDomain(SeasonJpaEntity entity) {
        return new Season(
            entity.getId(),
            entity.getLeagueId(),
            entity.getYear(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.isCurrent()
        );
    }

    public SeasonJpaEntity toJpaEntity(Season season) {
        return new SeasonJpaEntity(
            season.getId(),
            season.getLeagueId(),
            season.getYear(),
            season.getStartDate(),
            season.getEndDate(),
            season.isCurrent()
        );
    }
}
