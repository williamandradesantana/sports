package io.github.williamandradesantana.sports.application.match;

import java.util.Optional;

public interface OddsProvider {
    Optional<ExternalOddsData> fetchByMatchAndBookmaker(Long matchExternalId, Long bookmakerExternalId);
}
