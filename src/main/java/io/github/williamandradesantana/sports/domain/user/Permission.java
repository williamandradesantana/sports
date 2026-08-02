package io.github.williamandradesantana.sports.domain.user;

import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPermissionDescriptionException;

import java.util.Objects;
import java.util.UUID;

public class Permission {
    private UUID id;
    private String description;

    public Permission() {}
    public Permission(UUID id, String description) {
        this.id = id;
        setDescription(description);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) throw new InvalidPermissionDescriptionException();
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
