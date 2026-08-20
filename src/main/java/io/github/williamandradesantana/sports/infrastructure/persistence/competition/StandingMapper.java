package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import io.github.williamandradesantana.sports.domain.competition.Standing;
import io.github.williamandradesantana.sports.domain.competition.StandingRecord;
import io.github.williamandradesantana.sports.domain.competition.StandingTrend;

public class StandingMapper {

    public Standing toDomain(StandingJpaEntity entity) {
        return new Standing(
                entity.getId(), entity.getSeasonId(), entity.getTeamId(), entity.getRank(), entity.getPoints(),
                entity.getGroupName(), entity.getForm(), StandingTrend.valueOf(entity.getTrend().toUpperCase()),
                entity.getDescription(), toDomainRecord(entity.getOverall()), toDomainRecord(entity.getHome()),
                toDomainRecord(entity.getAway()), entity.getLastUpdatedAt()
        );
    }

    public StandingJpaEntity toJpaEntity(Standing standing) {
        return new StandingJpaEntity(
                standing.getId(), standing.getSeasonId(), standing.getTeamId(), standing.getRank(),
                standing.getPoints(), standing.getGroupName(), standing.getForm().orElse(null),
                standing.getTrend().name(), standing.getDescription().orElse(null),
                toEmbeddable(standing.getOverall()), toEmbeddable(standing.getHome()), toEmbeddable(standing.getAway()),
                standing.getLastUpdatedAt()
        );
    }

    private StandingRecord toDomainRecord(StandingRecordEmbeddable embeddable) {
        return new StandingRecord(embeddable.getPlayed(), embeddable.getWin(), embeddable.getDraw(),
                embeddable.getLose(), embeddable.getGoalsFor(), embeddable.getGoalsAgainst());
    }

    private StandingRecordEmbeddable toEmbeddable(StandingRecord record) {
        return new StandingRecordEmbeddable(record.played(), record.win(), record.draw(),
                record.lose(), record.goalsFor(), record.goalsAgainst());
    }
}
