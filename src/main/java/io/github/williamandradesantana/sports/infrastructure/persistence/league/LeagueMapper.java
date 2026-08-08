package io.github.williamandradesantana.sports.infrastructure.persistence.league;

import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueType;

public class LeagueMapper {

    public League toDomain(LeagueJpaEntity entity) {
        Country country = new Country(
            entity.getCountry().getName(),
            entity.getCountry().getCode(),
            entity.getCountry().getFlagUrl()
        );
        return new League(
                entity.getId(),
                entity.getExternalId(),
                entity.getName(),
                LeagueType.valueOf(entity.getType()),
                entity.getLogoUrl(),
                country
        );
    }

    public LeagueJpaEntity toJpaEntity(League league) {
        CountryEmbeddable country = new CountryEmbeddable();
        country.setName(league.getCountry().name());
        country.setCode(league.getCountry().code());
        country.setFlagUrl(league.getCountry().flagUrl());

        return new LeagueJpaEntity(
            league.getId(),
            league.getExternalId(),
            league.getName(),
            league.getType().name(),
            league.getLogoUrl(),
            country
        );
    }
}
