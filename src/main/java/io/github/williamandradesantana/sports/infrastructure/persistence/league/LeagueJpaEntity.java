package io.github.williamandradesantana.sports.infrastructure.persistence.league;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_leagues")
public class LeagueJpaEntity {

    @Id
    private UUID id;

    @Column(name = "external_id", unique = true, nullable = false)
    private Long externalId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "logo_url")
    private String logoUrl;

    @Embedded
    private CountryEmbeddable country;


    public LeagueJpaEntity() {}

    public LeagueJpaEntity(UUID id, Long externalId, String name, String type, String logoUrl, CountryEmbeddable country) {
        this.id = id;
        this.externalId = externalId;
        this.name = name;
        this.type = type;
        this.logoUrl = logoUrl;
        this.country = country;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public CountryEmbeddable getCountry() {
        return country;
    }

    public void setCountry(CountryEmbeddable country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        LeagueJpaEntity that = (LeagueJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
