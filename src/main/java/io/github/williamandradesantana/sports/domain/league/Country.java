package io.github.williamandradesantana.sports.domain.league;

import io.github.williamandradesantana.sports.domain.league.exceptions.InvalidCountryException;

public record Country(String name, String code, String flagUrl) {
    public Country {
        if (name == null || name.isBlank())
            throw new InvalidCountryException("Country name cannot be null or blank");
    }
}
