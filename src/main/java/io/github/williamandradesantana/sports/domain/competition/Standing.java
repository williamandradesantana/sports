package io.github.williamandradesantana.sports.domain.competition;

import io.github.williamandradesantana.sports.domain.competition.exceptions.InvalidStandingException;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Standing {

    private final UUID id;
    private final UUID seasonId;
    private final UUID teamId;
    private int rank;
    private int points;
    private String groupName;
    private String form;
    private StandingTrend trend;
    private String description;
    private StandingRecord overall;
    private StandingRecord home;
    private StandingRecord away;
    private OffsetDateTime lastUpdatedAt;

    public Standing(UUID id, UUID seasonId, UUID teamId, int rank, int points, String groupName, String form, StandingTrend trend, String description, StandingRecord overall, StandingRecord home, StandingRecord away, OffsetDateTime lastUpdatedAt) {
        if (seasonId == null) throw new InvalidStandingException("Standing must belong to a season");
        if (teamId == null) throw new InvalidStandingException("Standing must belong to a team");
        if (rank <= 0) throw new InvalidStandingException("Rank must be positive");
        this.id = id;
        this.seasonId = seasonId;
        this.teamId = teamId;
        this.rank = rank;
        this.points = points;
        this.groupName = groupName;
        this.form = form;
        this.trend = trend;
        this.description = description;
        this.overall = overall;
        this.home = home;
        this.away = away;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public void updateFromExternalSource(
            int rank, int points, String groupName, String form, StandingTrend trend,
            String description, StandingRecord overall, StandingRecord home,
            StandingRecord away, OffsetDateTime lastUpdatedAt
    ) {
        if (rank <= 0) throw new InvalidStandingException("Rank must be positive");
        this.rank = rank;
        this.points = points;
        this.groupName = groupName;
        this.form = form;
        this.trend = trend;
        this.description = description;
        this.overall = overall;
        this.home = home;
        this.away = away;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public UUID getId() { return id; }
    public UUID getSeasonId() { return seasonId; }
    public UUID getTeamId() { return teamId; }
    public int getRank() { return rank; }
    public int getPoints() { return points; }
    public String getGroupName() { return groupName; }
    public Optional<String> getForm() { return Optional.ofNullable(form); }
    public StandingTrend getTrend() { return trend; }
    public Optional<String> getDescription() { return Optional.ofNullable(description); }
    public StandingRecord getOverall() { return overall; }
    public StandingRecord getHome() { return home; }
    public StandingRecord getAway() { return away; }
    public OffsetDateTime getLastUpdatedAt() { return lastUpdatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Standing that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
