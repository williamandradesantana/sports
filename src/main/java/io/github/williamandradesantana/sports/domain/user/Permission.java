package io.github.williamandradesantana.sports.domain.user;

import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPermissionDescription;

import java.util.UUID;

public class Permission {
    private UUID id;
    private String description;

    public Permission() {}
    public Permission(UUID id, String description) {
        if (description == null || description.isBlank()) throw new InvalidPermissionDescription();
        this.id = id;
        this.description = description;
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
        this.description = description;
    }
}
