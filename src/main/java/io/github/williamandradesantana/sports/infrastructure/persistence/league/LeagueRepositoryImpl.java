package io.github.williamandradesantana.sports.infrastructure.persistence.league;

import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LeagueRepositoryImpl implements LeagueRepository {

    private final LeagueJpaRepository jpaRepository;
    private final LeagueMapper mapper;

    public LeagueRepositoryImpl(LeagueJpaRepository jpaRepository, LeagueMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<League> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<League> findByExternalId(Long externalId) {
        return jpaRepository.findByExternalId(externalId).map(mapper::toDomain);
    }

    @Override
    public List<League> finAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void save(League league) {
        jpaRepository.save(mapper.toJpaEntity(league));
    }
}
