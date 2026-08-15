package io.github.williamandradesantana.sports.interfaces.match.dto;

import io.github.williamandradesantana.sports.domain.team.Team;

import java.util.UUID;

public record TeamRefResponse(UUID id, String name, String logoUrl) {
    public static TeamRefResponse from(Team team) {
        return new TeamRefResponse(team.getId(), team.getName(), team.getLogoUrl());
    }
}
