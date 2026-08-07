package io.github.williamandradesantana.sports.domain.league;

import io.github.williamandradesantana.sports.domain.league.exceptions.InvalidLeagueNameException;
import io.github.williamandradesantana.sports.domain.shared.exceptions.InvalidExternalIdException;

import java.util.Objects;
import java.util.UUID;

public class League {
    private final UUID id;
    private final Long externalId;
    private String name;
    private LeagueType type;
    private String logoUrl;
    private Country country;

    public League(UUID id, Long externalId, String name, LeagueType type, String logoUrl, Country country) {
        if (externalId == null || externalId <= 0)
            throw new InvalidExternalIdException("External id must be a positive number");
        this.id = id;
        this.externalId = externalId;
        setName(name);
        this.type = type;
        this.logoUrl = logoUrl;
        this.country = country;
    }

    public UUID getId() {
        return id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new InvalidLeagueNameException("League name cannot be null or blank");
        this.name = name;
    }

    public void updateFromExternalSource(String name, LeagueType type, String logoUrl, Country country) {
        setName(name);
        this.type = type;
        this.logoUrl = logoUrl;
        this.country = country;
    }

    public LeagueType getType() {
        return type;
    }



    public void setType(LeagueType type) {
        this.type = type;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        League league = (League) o;
        return Objects.equals(id, league.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
