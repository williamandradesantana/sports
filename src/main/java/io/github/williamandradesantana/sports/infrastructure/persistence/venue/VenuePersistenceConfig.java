package io.github.williamandradesantana.sports.infrastructure.persistence.venue;

import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VenuePersistenceConfig {

    @Bean
    public VenueMapper venueMapper() {
        return new VenueMapper();
    }

    @Bean
    public VenueRepository venueRepository(VenueJpaRepository jpaRepository, VenueMapper mapper) {
        return new VenueRepositoryImpl(jpaRepository, mapper);
    }
}
