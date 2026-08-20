package io.github.williamandradesantana.sports.interfaces.shared.dto;

import java.util.List;

public record ListResponse<T>(List<T> content) {
    public static <T> ListResponse<T> of(List<T> content) {
        return new ListResponse<>(content);
    }
}
