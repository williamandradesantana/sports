package io.github.williamandradesantana.sports.domain.competition;

import io.github.williamandradesantana.sports.domain.competition.exceptions.InvalidSeasonException;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Season {

    private final UUID id;
    private final UUID leagueId;
    private final int year;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;

    public Season(UUID id, UUID leagueId, int year, LocalDate startDate, LocalDate endDate, boolean current) {
        if (leagueId == null) throw new InvalidSeasonException("Season must belong to a league");
        if (year < 1800) throw new InvalidSeasonException("Season year is invalid: " + year);
        if (startDate != null && endDate != null && !endDate.isAfter(startDate))
            throw new InvalidSeasonException("Season end data must be after start date");
        this.id = id;
        this.leagueId = leagueId;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.current = current;
    }

    public void markAsCurrent() {
        this.current = true;
    }

    public void markAsFinished() {
        this.current = false;
    }

    public UUID getId() {
        return id;
    }

    public UUID getLeagueId() {
        return leagueId;
    }

    public int getYear() {
        return year;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Season season = (Season) o;
        return Objects.equals(id, season.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
