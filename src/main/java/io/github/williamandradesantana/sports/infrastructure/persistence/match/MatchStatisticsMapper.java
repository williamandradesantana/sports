package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import io.github.williamandradesantana.sports.domain.match.MatchStatistics;

public class MatchStatisticsMapper {

    public MatchStatistics toDomain(MatchStatisticsJpaEntity entity) {
        return new MatchStatistics(
            entity.getId(), entity.getMatchId(), entity.getTeamId(), entity.getShotsOnGoal(), entity.getShotsOffGoal(),
            entity.getTotalShots(), entity.getBlockedShots(), entity.getShotsInsideBox(), entity.getShotsOutsideBox(),
            entity.getFouls(), entity.getCornerKicks(), entity.getOffsides(), entity.getBallPossessionPercentage(),
            entity.getYellowCards(), entity.getRedCards(), entity.getGoalkeeperSaves(), entity.getTotalPasses(),
            entity.getPassesAccurate(), entity.getPassesAccuracyPercentage()
        );
    }

    public MatchStatisticsJpaEntity toJpaEntity(MatchStatistics stats) {
        return new MatchStatisticsJpaEntity(
            stats.getId(), stats.getMatchId(), stats.getTeamId(),
            stats.getShotsOnGoal().orElse(null), stats.getShotsOffGoal().orElse(null),
            stats.getTotalShots().orElse(null), stats.getBlockedShots().orElse(null),
            stats.getShotsInsideBox().orElse(null), stats.getShotsOutsideBox().orElse(null),
            stats.getFouls().orElse(null), stats.getCornerKicks().orElse(null),
            stats.getOffsides().orElse(null), stats.getBallPossessionPercentage().orElse(null),
            stats.getYellowCards().orElse(null), stats.getRedCards().orElse(null),
            stats.getGoalkeeperSaves().orElse(null), stats.getTotalPasses().orElse(null),
            stats.getPassesAccurate().orElse(null), stats.getPassesAccuracyPercentage().orElse(null)
        );
    }
}
