package io.github.williamandradesantana.sports.domain.match;

import io.github.williamandradesantana.sports.domain.match.exceptions.InvalidMatchStatisticsException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class MatchStatistics {

    private final UUID id;
    private final UUID matchId;
    private final UUID teamId;
    private Integer shotsOnGoal;
    private Integer shotsOffGoal;
    private Integer totalShots;
    private Integer blockedShots;
    private Integer shotsInsideBox;
    private Integer shotsOutsideBox;
    private Integer fouls;
    private Integer cornerKicks;
    private Integer offsides;
    private Integer ballPossessionPercentage;
    private Integer yellowCards;
    private Integer redCards;
    private Integer goalkeeperSaves;
    private Integer totalPasses;
    private Integer passesAccurate;
    private Integer passesAccuracyPercentage;

    public MatchStatistics(UUID id, UUID matchId, UUID teamId, Integer shotsOnGoal, Integer shotsOffGoal, Integer totalShots, Integer blockedShots, Integer shotsInsideBox, Integer shotsOutsideBox, Integer fouls, Integer cornerKicks, Integer offsides, Integer ballPossessionPercentage, Integer yellowCards, Integer redCards, Integer goalkeeperSaves, Integer totalPasses, Integer passesAccurate, Integer passesAccuracyPercentage) {
        if (matchId == null) throw new InvalidMatchStatisticsException("MatchStatistics must belong to a match");
        if (teamId == null) throw new InvalidMatchStatisticsException("MatchStatistics must belong to a team");
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

    public void updateFromExternalSource(Integer shotsOnGoal, Integer shotsOffGoal, Integer totalShots,
                                         Integer blockedShots, Integer shotsInsideBox, Integer shotsOutsideBox,
                                         Integer fouls, Integer cornerKicks, Integer offsides,
                                         Integer ballPossessionPercentage, Integer yellowCards, Integer redCards,
                                         Integer goalkeeperSaves, Integer totalPasses, Integer passesAccurate,
                                         Integer passesAccuracyPercentage) {
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
    public Optional<Integer> getShotsOnGoal() { return Optional.ofNullable(shotsOnGoal); }
    public Optional<Integer> getShotsOffGoal() { return Optional.ofNullable(shotsOffGoal); }
    public Optional<Integer> getTotalShots() { return Optional.ofNullable(totalShots); }
    public Optional<Integer> getBlockedShots() { return Optional.ofNullable(blockedShots); }
    public Optional<Integer> getShotsInsideBox() { return Optional.ofNullable(shotsInsideBox); }
    public Optional<Integer> getShotsOutsideBox() { return Optional.ofNullable(shotsOutsideBox); }
    public Optional<Integer> getFouls() { return Optional.ofNullable(fouls); }
    public Optional<Integer> getCornerKicks() { return Optional.ofNullable(cornerKicks); }
    public Optional<Integer> getOffsides() { return Optional.ofNullable(offsides); }
    public Optional<Integer> getBallPossessionPercentage() { return Optional.ofNullable(ballPossessionPercentage); }
    public Optional<Integer> getYellowCards() { return Optional.ofNullable(yellowCards); }
    public Optional<Integer> getRedCards() { return Optional.ofNullable(redCards); }
    public Optional<Integer> getGoalkeeperSaves() { return Optional.ofNullable(goalkeeperSaves); }
    public Optional<Integer> getTotalPasses() { return Optional.ofNullable(totalPasses); }
    public Optional<Integer> getPassesAccurate() { return Optional.ofNullable(passesAccurate); }
    public Optional<Integer> getPassesAccuracyPercentage() { return Optional.ofNullable(passesAccuracyPercentage); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchStatistics that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
