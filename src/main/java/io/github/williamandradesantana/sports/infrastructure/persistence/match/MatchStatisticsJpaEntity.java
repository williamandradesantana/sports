package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_match_statistics")
public class MatchStatisticsJpaEntity {

    @Id
    private UUID id;

    @Column(name = "match_id", nullable = false)
    private UUID matchId;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "shots_on_goal")
    private Integer shotsOnGoal;

    @Column(name = "shots_off_goal")
    private Integer shotsOffGoal;

    @Column(name = "total_shots")
    private Integer totalShots;

    @Column(name = "blocked_shots")
    private Integer blockedShots;

    @Column(name = "shots_inside_box")
    private Integer shotsInsideBox;

    @Column(name = "shots_outside_box")
    private Integer shotsOutsideBox;

    @Column(name = "fouls")
    private Integer fouls;

    @Column(name = "corner_kicks")
    private Integer cornerKicks;

    @Column(name = "offsides")
    private Integer offsides;

    @Column(name = "ball_possession_percentage")
    private Integer ballPossessionPercentage;

    @Column(name = "yellow_cards")
    private Integer yellowCards;

    @Column(name = "red_cards")
    private Integer redCards;

    @Column(name = "goalkeeper_saves")
    private Integer goalkeeperSaves;

    @Column(name = "total_passes")
    private Integer totalPasses;

    @Column(name = "passes_accurate")
    private Integer passesAccurate;

    @Column(name = "passes_accuracy_percentage")
    private Integer passesAccuracyPercentage;

    protected MatchStatisticsJpaEntity() {}

    public MatchStatisticsJpaEntity(UUID id, UUID matchId, UUID teamId, Integer shotsOnGoal, Integer shotsOffGoal,
                                    Integer totalShots, Integer blockedShots, Integer shotsInsideBox,
                                    Integer shotsOutsideBox, Integer fouls, Integer cornerKicks, Integer offsides,
                                    Integer ballPossessionPercentage, Integer yellowCards, Integer redCards,
                                    Integer goalkeeperSaves, Integer totalPasses, Integer passesAccurate,
                                    Integer passesAccuracyPercentage) {
        this.id = id;
        this.matchId = matchId;
        this.teamId = teamId;
        this.shotsOnGoal = shotsOnGoal;
        this.shotsOffGoal = shotsOffGoal;
        this.totalShots = totalShots;
        this.blockedShots = blockedShots;
        this.shotsInsideBox = shotsInsideBox;
        this.shotsOutsideBox = shotsOutsideBox;
        this.fouls = fouls;
        this.cornerKicks = cornerKicks;
        this.offsides = offsides;
        this.ballPossessionPercentage = ballPossessionPercentage;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.goalkeeperSaves = goalkeeperSaves;
        this.totalPasses = totalPasses;
        this.passesAccurate = passesAccurate;
        this.passesAccuracyPercentage = passesAccuracyPercentage;
    }

    public UUID getId() { return id; }
    public UUID getMatchId() { return matchId; }
    public UUID getTeamId() { return teamId; }
    public Integer getShotsOnGoal() { return shotsOnGoal; }
    public Integer getShotsOffGoal() { return shotsOffGoal; }
    public Integer getTotalShots() { return totalShots; }
    public Integer getBlockedShots() { return blockedShots; }
    public Integer getShotsInsideBox() { return shotsInsideBox; }
    public Integer getShotsOutsideBox() { return shotsOutsideBox; }
    public Integer getFouls() { return fouls; }
    public Integer getCornerKicks() { return cornerKicks; }
    public Integer getOffsides() { return offsides; }
    public Integer getBallPossessionPercentage() { return ballPossessionPercentage; }
    public Integer getYellowCards() { return yellowCards; }
    public Integer getRedCards() { return redCards; }
    public Integer getGoalkeeperSaves() { return goalkeeperSaves; }
    public Integer getTotalPasses() { return totalPasses; }
    public Integer getPassesAccurate() { return passesAccurate; }
    public Integer getPassesAccuracyPercentage() { return passesAccuracyPercentage; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchStatisticsJpaEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
