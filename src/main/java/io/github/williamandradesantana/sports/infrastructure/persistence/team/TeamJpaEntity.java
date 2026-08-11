package io.github.williamandradesantana.sports.infrastructure.persistence.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_teams")
public class TeamJpaEntity {

    @Id
    private UUID id;

    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "founded")
    private Integer founded;

    @Column(name = "national", nullable = false)
    private boolean national;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "venue_id")
    private UUID venueId;

    protected TeamJpaEntity(){}

    public TeamJpaEntity(UUID id, Long externalId, String name, String code, String countryName, Integer founded, boolean national, String logoUrl, UUID venueId) {
        this.id = id;
        this.externalId = externalId;
        this.name = name;
        this.code = code;
        this.countryName = countryName;
        this.founded = founded;
        this.national = national;
        this.logoUrl = logoUrl;
        this.venueId = venueId;
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

    public Integer getFounded() {
        return founded;
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

    public UUID getVenueId() {
        return venueId;
    }

    public void setVenueId(UUID venueId) {
        this.venueId = venueId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        TeamJpaEntity that = (TeamJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
