package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import io.github.williamandradesantana.sports.domain.match.MatchStatistics;
import io.github.williamandradesantana.sports.domain.match.MatchStatisticsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MatchStatisticsRepositoryImpl implements MatchStatisticsRepository {

    private final MatchStatisticsJpaRepository jpaRepository;
    private final MatchStatisticsMapper mapper;

    public MatchStatisticsRepositoryImpl(MatchStatisticsJpaRepository jpaRepository, MatchStatisticsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<MatchStatistics> findByMatchIdAndTeamId(UUID matchId, UUID teamId) {
        return jpaRepository.findByMatchIdAndTeamId(matchId, teamId).map(mapper::toDomain);
    }

    @Override
    public List<MatchStatistics> findByMatchId(UUID matchId) {
        return jpaRepository.findByMatchId(matchId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void save(MatchStatistics statistics) {
        jpaRepository.save(mapper.toJpaEntity(statistics));
    }
}
