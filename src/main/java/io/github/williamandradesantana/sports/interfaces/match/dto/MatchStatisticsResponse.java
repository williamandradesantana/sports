package io.github.williamandradesantana.sports.interfaces.match.dto;

import io.github.williamandradesantana.sports.domain.match.MatchStatistics;

public record MatchStatisticsResponse(
    Integer shotsOnGoal, Integer shotsOffGoal, Integer totalShots,
    Integer blockedShots, Integer shotsInsideBox, Integer shotsOutsideBox,
    Integer fouls, Integer cornerKicks, Integer offsides,
    Integer ballPossessionPercentage, Integer yellowCards, Integer redCards,
    Integer goalkeeperSaves, Integer totalPasses, Integer passesAccurate,
    Integer passesAccuracyPercentage
) {
    public static MatchStatisticsResponse from(MatchStatistics stats) {
        return new MatchStatisticsResponse(
            stats.getShotsOnGoal().orElse(null), stats.getShotsOffGoal().orElse(null),
            stats.getTotalShots().orElse(null), stats.getBlockedShots().orElse(null),
            stats.getShotsInsideBox().orElse(null), stats.getShotsOutsideBox().orElse(null),
            stats.getFouls().orElse(null), stats.getCornerKicks().orElse(null),
            stats.getOffsides().orElse(null),
            stats.getBallPossessionPercentage().orElse(null), stats.getYellowCards().orElse(null),
            stats.getRedCards().orElse(null), stats.getGoalkeeperSaves().orElse(null),
            stats.getTotalPasses().orElse(null), stats.getPassesAccurate().orElse(null),
            stats.getPassesAccuracyPercentage().orElse(null)
        );
    }
}
