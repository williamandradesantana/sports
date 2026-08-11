package io.github.williamandradesantana.sports.domain.team;

import io.github.williamandradesantana.sports.domain.shared.exceptions.InvalidExternalIdException;
import io.github.williamandradesantana.sports.domain.team.exceptions.InvalidTeamNameException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Team {

    private final UUID id;
    private final Long externalId;
    private String name;
    private String code;
    private String countryName;
    private Integer founded;
    private boolean national;
    private String logoUrl;
    private UUID venueId;

    public Team(UUID id, Long externalId, String name, String code, String countryName, Integer founded, boolean national, String logoUrl, UUID venueId) {
        if (externalId == null || externalId <= 0)
            throw new InvalidExternalIdException("External id must be a positive number");
        this.id = id;
        this.externalId = externalId;
        setName(name);
        this.code = code;
        this.countryName = countryName;
        this.founded = founded;
        this.national = national;
        this.logoUrl = logoUrl;
        this.venueId = venueId;
    }

    public void updateFromExternalSource(
            String name, String code, String countryName, Integer founded, String logoUrl, UUID venueId
    ) {
        setName(name);
        this.code = code;
        this.countryName = countryName;
        this.founded = founded;
        this.logoUrl = logoUrl;
        this.venueId = venueId;
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
        if (name == null || name.isBlank()) throw new InvalidTeamNameException("Team name cannot be null or blank!");
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Optional<Integer> getFounded() {
        return Optional.ofNullable(founded);
    }

    public void setFounded(Integer founded) {
        this.founded = founded;
    }

    public boolean isNational() {
        return national;
    }

    public void setNational(boolean national) {
        this.national = national;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Optional<UUID> getVenueId() {
        return Optional.ofNullable(venueId);
    }

    public void setVenueId(UUID venueId) {
        this.venueId = venueId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
