package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import io.github.williamandradesantana.sports.domain.match.Odds;

public class OddsMapper {

    public Odds toDomain(OddsJpaEntity entity) {
        return new Odds(
            entity.getId(), entity.getMatchId(), entity.getBookmakerExternalId(), entity.getBookmakerName(),
            entity.getCapturedAt(), entity.getHomeWinOdd(), entity.getDrawOdd(), entity.getAwayWinOdd(),
            entity.getOverGoalsOdd(), entity.getUnderGoalsOdd(), entity.getBothTeamsScoreYesOdd(),
            entity.getBothTeamsScoreNoOdd()
        );
    }

    public OddsJpaEntity toJpaEntity(Odds odds) {
        return new OddsJpaEntity(
            odds.getId(), odds.getMatchId(), odds.getBookmakerExternalId(), odds.getBookmakerName(),
            odds.getCapturedAt(), odds.getHomeWinOdd().orElse(null), odds.getDrawOdd().orElse(null),
            odds.getAwayWinOdd().orElse(null), odds.getOverGoalsOdd().orElse(null),
            odds.getUnderGoalsOdd().orElse(null), odds.getBothTeamsScoreYesOdd().orElse(null),
            odds.getBothTeamsScoreNoOdd().orElse(null)
        );
    }
}
