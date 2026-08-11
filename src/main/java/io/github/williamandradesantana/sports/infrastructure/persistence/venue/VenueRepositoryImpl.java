package io.github.williamandradesantana.sports.infrastructure.persistence.venue;

import io.github.williamandradesantana.sports.domain.venue.Venue;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;

import java.util.Optional;
import java.util.UUID;

public class VenueRepositoryImpl implements VenueRepository {

    private final VenueJpaRepository jpaRepository;
    private final VenueMapper mapper;

    public VenueRepositoryImpl(VenueJpaRepository jpaRepository, VenueMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Venue> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Venue> findByExternalId(Long externalId) {
        return jpaRepository.findByExternalId(externalId).map(mapper::toDomain);
    }

    @Override
    public void save(Venue venue) {
        jpaRepository.save(mapper.toJpaEntity(venue));
    }
}
