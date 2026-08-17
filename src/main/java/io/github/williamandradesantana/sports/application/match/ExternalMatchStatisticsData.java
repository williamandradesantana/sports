package io.github.williamandradesantana.sports.application.match;

public record ExternalMatchStatisticsData(
    Long teamExternalId, Integer shotsOnGoal, Integer shotsOffGoal,
    Integer totalShots, Integer blockedShots, Integer shotsInsideBox,
    Integer shotsOutsideBox, Integer fouls, Integer cornerKicks,
    Integer offsides, Integer ballPossessionPercentage, Integer yellowCards,
    Integer redCards, Integer goalkeeperSaves, Integer totalPasses,
    Integer passesAccurate, Integer passesAccuracyPercentage
) {
}
