package io.github.williamandradesantana.sports.domain.match;

import io.github.williamandradesantana.sports.domain.match.exceptions.InvalidMatchException;
import io.github.williamandradesantana.sports.domain.shared.exceptions.InvalidExternalIdException;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Match {
    private final UUID id;
    private final Long externalId;
    private final UUID leagueId;
    private final UUID seasonId;
    private final UUID homeTeamId;
    private final UUID awayTeamId;
    private UUID venueId;
    private OffsetDateTime matchDate;
    private MatchStatus status;
    private Integer homeGoals;
    private Integer awayGoals;
    private String round;
    private String referee;

    public Match(UUID id, Long externalId, UUID leagueId, UUID seasonId, UUID homeTeamId, UUID awayTeamId, UUID venueId, OffsetDateTime matchDate, MatchStatus status, Integer homeGoals, Integer awayGoals, String round, String referee) {
        if (externalId == null || externalId <= 0)
            throw new InvalidExternalIdException("External id must be number positive");
        if (leagueId == null || seasonId == null)
            throw new InvalidMatchException("Match must belong to a league and a season");
        if (homeTeamId == null || awayTeamId == null)
            throw new InvalidMatchException("Match must have both home and away teams");
        if (homeTeamId.equals(awayTeamId))
            throw new InvalidMatchException("Home and away teams cannot be the same");
        if (matchDate == null)
            throw new InvalidMatchException("Match date cannot be null");
        if (status == null) throw new InvalidMatchException("Match status cannot be null");

        this.id = id;
        this.externalId = externalId;
        this.leagueId = leagueId;
        this.seasonId = seasonId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.venueId = venueId;
        this.matchDate = matchDate;
        this.status = status;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.round = round;
        this.referee = referee;
    }

    public void updateFromExternalSource(
            UUID venueId, OffsetDateTime matchDate, MatchStatus status, Integer homeGoals,
            Integer awayGoals, String round, String referee
    ) {
        this.venueId = venueId;
        this.matchDate = matchDate;
        this.status = status;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.round = round;
        this.referee = referee;
    }

    public boolean isFinished() {
        return status == MatchStatus.FINISHED;
    }

    public Optional<UUID> getWinnerId() {
        if (!isFinished() || homeGoals == null || awayGoals == null || homeGoals.equals(awayGoals))
            return Optional.empty();
        return Optional.of(homeGoals > awayGoals ? homeTeamId : awayTeamId);
    }

    public UUID getId() {
        return id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public UUID getLeagueId() {
        return leagueId;
    }

    public UUID getSeasonId() {
        return seasonId;
    }

    public UUID getHomeTeamId() {
        return homeTeamId;
    }

    public UUID getAwayTeamId() {
        return awayTeamId;
    }

    public Optional<UUID> getVenueId() {
        return Optional.ofNullable(venueId);
    }

    public void setVenueId(UUID venueId) {
        this.venueId = venueId;
    }

    public OffsetDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(OffsetDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public Optional<Integer> getHomeGoals() {
        return Optional.ofNullable(homeGoals);
    }

    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }

    public Optional<Integer> getAwayGoals() {
        return Optional.ofNullable(awayGoals);
    }

    public void setAwayGoals(Integer awayGoals) {
        this.awayGoals = awayGoals;
    }

    public Optional<String> getRound() {
        return Optional.ofNullable(round);
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Optional<String> getReferee() {
        return Optional.ofNullable(referee);
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;
        return Objects.equals(id, match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
