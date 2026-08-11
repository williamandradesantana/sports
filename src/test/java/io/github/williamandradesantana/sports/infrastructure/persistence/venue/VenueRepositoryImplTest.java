package io.github.williamandradesantana.sports.infrastructure.persistence.venue;

import io.github.williamandradesantana.sports.domain.venue.Venue;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(VenuePersistenceConfig.class)
class VenueRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private VenueRepository repository;

    private Venue venue;

    @BeforeEach
    void setup() {
        venue = new Venue(
            UUID.randomUUID(), 556L, "Old Trafford", "Sir Matt Busby Way",
            "Manchester", 76212, "grass", "https://media.api-sports.io/football/venues/556.png"
        );
    }

    @AfterEach
    void afterEach() {
        venue = null;
    }

    @Test
    @DisplayName("Test: saving a venue without capacity should round-trip correctly")
    void test_SavingVenueWithoutCapacity_ShouldRoundTrip() {
        venue.setCapacity(null);

        repository.save(venue);

        Optional<Venue> found = repository.findByExternalId(556L);

        assertTrue(found.isPresent());
        assertNull(found.get().getCapacity());
    }
}