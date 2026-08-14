package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_matches")
public class MatchJpaEntity {

    @Id
    private UUID id;

    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Column(name = "league_id", nullable = false)
    private UUID leagueId;

    @Column(name = "season_id", nullable = false)
    private UUID seasonId;

    @Column(name = "home_team_id", nullable = false)
    private UUID homeTeamId;

    @Column(name = "away_team_id", nullable = false)
    private UUID awayTeamId;

    @Column(name = "venue_id")
    private UUID venueId;

    @Column(name = "match_date", nullable = false)
    private OffsetDateTime matchDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "home_goals")
    private Integer homeGoals;

    @Column(name = "away_goals")
    private Integer awayGoals;

    @Column(name = "round")
    private String round;

    @Column(name = "referee")
    private String referee;

    protected MatchJpaEntity() {}

    public MatchJpaEntity(UUID id, Long externalId, UUID leagueId, UUID seasonId, UUID homeTeamId, UUID awayTeamId, UUID venueId, OffsetDateTime matchDate, String status, Integer homeGoals, Integer awayGoals, String round, String referee) {
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

    public UUID getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(UUID leagueId) {
        this.leagueId = leagueId;
    }

    public UUID getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(UUID seasonId) {
        this.seasonId = seasonId;
    }

    public UUID getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(UUID homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public UUID getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(UUID awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public UUID getVenueId() {
        return venueId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }

    public Integer getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(Integer awayGoals) {
        this.awayGoals = awayGoals;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        MatchJpaEntity that = (MatchJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
