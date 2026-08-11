package io.github.williamandradesantana.sports.infrastructure.persistence.team;

import io.github.williamandradesantana.sports.domain.team.Team;

public class TeamMapper {

    public Team toDomain(TeamJpaEntity entity) {
        return new Team(
            entity.getId(),
            entity.getExternalId(),
            entity.getName(),
            entity.getCode(),
            entity.getCountryName(),
            entity.getFounded(),
            entity.isNational(),
            entity.getLogoUrl(),
            entity.getVenueId()
        );
    }

    public TeamJpaEntity toJpaEntity(Team team) {
        return new TeamJpaEntity(
                team.getId(),
                team.getExternalId(),
                team.getName(),
                team.getCode(),
                team.getCountryName(),
                team.getFounded().orElse(null),
                team.isNational(),
                team.getLogoUrl(),
                team.getVenueId().orElse(null)
        );
    }
}
