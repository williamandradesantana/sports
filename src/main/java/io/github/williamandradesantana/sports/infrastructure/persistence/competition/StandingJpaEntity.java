package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_standings")
public class StandingJpaEntity {

    @Id
    private UUID id;

    @Column(name = "season_id", nullable = false)
    private UUID seasonId;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "rank", nullable = false)
    private int rank;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "form")
    private String form;

    @Column(name = "trend", nullable = false)
    private String trend;

    @Column(name = "description")
    private String description;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "played", column = @Column(name = "overall_played")),
        @AttributeOverride(name = "win", column = @Column(name = "overall_win")),
        @AttributeOverride(name = "draw", column = @Column(name = "overall_draw")),
        @AttributeOverride(name = "lose", column = @Column(name = "overall_lose")),
        @AttributeOverride(name = "goalsFor", column = @Column(name = "overall_goals_for")),
        @AttributeOverride(name = "goalsAgainst", column = @Column(name = "overall_goals_against"))
    })
    private StandingRecordEmbeddable overall;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "played", column = @Column(name = "home_played")),
        @AttributeOverride(name = "win", column = @Column(name = "home_win")),
        @AttributeOverride(name = "draw", column = @Column(name = "home_draw")),
        @AttributeOverride(name = "lose", column = @Column(name = "home_lose")),
        @AttributeOverride(name = "goalsFor", column = @Column(name = "home_goals_for")),
        @AttributeOverride(name = "goalsAgainst", column = @Column(name = "home_goals_against"))
    })
    private StandingRecordEmbeddable home;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "played", column = @Column(name = "away_played")),
        @AttributeOverride(name = "win", column = @Column(name = "away_win")),
        @AttributeOverride(name = "draw", column = @Column(name = "away_draw")),
        @AttributeOverride(name = "lose", column = @Column(name = "away_lose")),
        @AttributeOverride(name = "goalsFor", column = @Column(name = "away_goals_for")),
        @AttributeOverride(name = "goalsAgainst", column = @Column(name = "away_goals_against"))
    })
    private StandingRecordEmbeddable away;

    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    protected StandingJpaEntity() {}

    public StandingJpaEntity(UUID id, UUID seasonId, UUID teamId, int rank, int points, String groupName,
                             String form, String trend, String description, StandingRecordEmbeddable overall,
                             StandingRecordEmbeddable home, StandingRecordEmbeddable away,
                             OffsetDateTime lastUpdatedAt) {
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

    public UUID getId() { return id; }
    public UUID getSeasonId() { return seasonId; }
    public UUID getTeamId() { return teamId; }
    public int getRank() { return rank; }
    public int getPoints() { return points; }
    public String getGroupName() { return groupName; }
    public String getForm() { return form; }
    public String getTrend() { return trend; }
    public String getDescription() { return description; }
    public StandingRecordEmbeddable getOverall() { return overall; }
    public StandingRecordEmbeddable getHome() { return home; }
    public StandingRecordEmbeddable getAway() { return away; }
    public OffsetDateTime getLastUpdatedAt() { return lastUpdatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StandingJpaEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
