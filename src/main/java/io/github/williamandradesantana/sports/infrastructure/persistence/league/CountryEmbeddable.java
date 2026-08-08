package io.github.williamandradesantana.sports.infrastructure.persistence.league;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CountryEmbeddable {

    @Column(name = "country_name", nullable = false)
    private String name;

    @Column(name = "country_code")
    private String code;

    @Column(name = "country_flag_url")
    private String flagUrl;

    protected CountryEmbeddable(){}

    public CountryEmbeddable(String name, String code, String flagUrl) {
        this.name = name;
        this.code = code;
        this.flagUrl = flagUrl;
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

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }
}
