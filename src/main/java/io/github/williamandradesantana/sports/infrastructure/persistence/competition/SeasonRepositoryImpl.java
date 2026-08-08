package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SeasonRepositoryImpl implements SeasonRepository {

    private final SeasonJpaRepository seasonRepository;
    private final SeasonMapper mapper;

    public SeasonRepositoryImpl(SeasonJpaRepository seasonRepository, SeasonMapper mapper) {
        this.seasonRepository = seasonRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Season> findByLeagueIdAndYear(UUID leagueId, int year) {
        return seasonRepository.findByLeagueIdAndYear(leagueId, year).map(mapper::toDomain);
    }

    @Override
    public Optional<Season> findCurrentByLeagueId(UUID id) {
        return seasonRepository.findByLeagueIdAndCurrentTrue(id).map(mapper::toDomain);
    }

    @Override
    public List<Season> findAllLeagueId(UUID leagueId) {
        return seasonRepository.findAllByLeagueId(leagueId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void save(Season season) {
        seasonRepository.save(mapper.toJpaEntity(season));
    }
}
