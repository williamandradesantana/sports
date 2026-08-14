package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchStatus;

public class MatchMapper {

    public Match toDomain(MatchJpaEntity entity) {
        return new Match(
            entity.getId(), entity.getExternalId(), entity.getLeagueId(), entity.getSeasonId(),
            entity.getHomeTeamId(), entity.getAwayTeamId(), entity.getVenueId(), entity.getMatchDate(),
            MatchStatus.valueOf(entity.getStatus()), entity.getHomeGoals(), entity.getAwayGoals(), entity.getRound(),
            entity.getReferee()
        );
    }

    public MatchJpaEntity toJpaEntity(Match match) {
        return new MatchJpaEntity(
            match.getId(), match.getExternalId(), match.getLeagueId(), match.getSeasonId(),
            match.getHomeTeamId(), match.getAwayTeamId(), match.getVenueId().orElse(null), match.getMatchDate(),
            match.getStatus().name(), match.getHomeGoals().orElse(null), match.getAwayGoals().orElse(null),
            match.getRound().orElse(null), match.getReferee().orElse(null)
        );
    }
}
