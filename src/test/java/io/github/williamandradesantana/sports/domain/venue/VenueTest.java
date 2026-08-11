package io.github.williamandradesantana.sports.domain.venue;

import io.github.williamandradesantana.sports.domain.shared.exceptions.InvalidExternalIdException;
import io.github.williamandradesantana.sports.domain.venue.exceptions.InvalidVenueCapacityException;
import io.github.williamandradesantana.sports.domain.venue.exceptions.InvalidVenueNameException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VenueTest {

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
    @DisplayName("Test: creating a venue with success")
    void test_CreatingAVenueWithSuccess() {
        assertEquals("Old Trafford", venue.getName());
        assertEquals(76212, venue.getCapacity());
    }

    @Test
    @DisplayName("Test: creating a venue without a capacity should succeed")
    void test_CreatingVenueWithoutCapacity_ShouldSucceed() {
        venue.setCapacity(null);

        assertNull(venue.getCapacity());
    }

    @Test
    @DisplayName("Test: creating a venue with non-positive capacity should throw")
    void test_CreatingVenueWithNonPositiveCapacity_ShouldThrow() {
        assertThrows(InvalidVenueCapacityException.class, () -> venue.setCapacity(-1));
    }

    @Test
    @DisplayName("Test: creating a venue with blank name should throw")
    void test_CreatingVenueWithBlankName_ShouldThrow() {
        assertThrows(InvalidVenueNameException.class, () -> venue.setName(""));
    }

    @Test
    @DisplayName("Test: creating a venue with invalid external id should throw")
    void test_CreatingVenueWithInvalidExternalId_ShouldThrow() {
        assertThrows(InvalidExternalIdException.class, () ->
            new Venue(UUID.randomUUID(), 0L, "Old Trafford", "address",
                    "city", 100, "grass", null
            )
        );
    }
}