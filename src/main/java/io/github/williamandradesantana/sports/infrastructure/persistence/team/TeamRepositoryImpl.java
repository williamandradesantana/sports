package io.github.williamandradesantana.sports.infrastructure.persistence.team;

import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public class TeamRepositoryImpl implements TeamRepository {

    private final TeamJpaRepository jpaRepository;
    private final TeamMapper mapper;

    public TeamRepositoryImpl(TeamJpaRepository jpaRepository, TeamMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Team> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Team> findByExternalId(Long externalId) {
        return jpaRepository.findByExternalId(externalId).map(mapper::toDomain);
    }

    @Override
    public Page<Team> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void save(Team team) {
        jpaRepository.save(mapper.toJpaEntity(team));
    }
}
