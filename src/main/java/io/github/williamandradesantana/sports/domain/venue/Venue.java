package io.github.williamandradesantana.sports.domain.venue;

import io.github.williamandradesantana.sports.domain.shared.exceptions.InvalidExternalIdException;
import io.github.williamandradesantana.sports.domain.venue.exceptions.InvalidVenueCapacityException;
import io.github.williamandradesantana.sports.domain.venue.exceptions.InvalidVenueNameException;

import java.util.Objects;
import java.util.UUID;

public class Venue {

    private final UUID id;
    private final Long externalId;
    private String name;
    private String address;
    private String city;
    private Integer capacity;
    private String surface;
    private String image;

    public Venue(UUID id, Long externalId, String name, String address, String city, Integer capacity, String surface, String image) {
        if (externalId == null || externalId <= 0)
            throw new InvalidExternalIdException("External id must be a positive number");
        this.id = id;
        this.externalId = externalId;
        setName(name);
        this.address = address;
        this.city = city;
        setCapacity(capacity);
        this.surface = surface;
        this.image = image;
    }

    public void updateFromExternalSource(
            String name, String address, String city, Integer capacity, String surface, String image
    ) {
        setName(name);
        this.address = address;
        this.city = city;
        setCapacity(capacity);
        this.surface = surface;
        this.image = image;
    }

    public UUID getId() {
        return id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new InvalidVenueNameException("Venue name cannot be null or blank");
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        if (capacity != null && capacity <= 0)
            throw new InvalidVenueCapacityException("Venue capacity must be positive when informed");
        this.capacity = capacity;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Venue venue = (Venue) o;
        return Objects.equals(id, venue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
