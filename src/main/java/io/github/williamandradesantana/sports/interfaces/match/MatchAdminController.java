package io.github.williamandradesantana.sports.interfaces.match;

import io.github.williamandradesantana.sports.application.integrity.AssessMatchIntegrityUseCase;
import io.github.williamandradesantana.sports.application.match.SyncMatchStatisticsUseCase;
import io.github.williamandradesantana.sports.application.match.SyncMatchUseCase;
import io.github.williamandradesantana.sports.application.match.SyncOddsUseCase;
import io.github.williamandradesantana.sports.interfaces.integrity.dto.IntegrityAssessmentResponse;
import io.github.williamandradesantana.sports.interfaces.match.dto.MatchResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/matches")
@PreAuthorize("hasAuthority('ADMIN')")
public class MatchAdminController {

    private final SyncMatchStatisticsUseCase syncMatchStatisticsUseCase;
    private final SyncMatchUseCase syncMatchUseCase;
    private final SyncOddsUseCase syncOddsUseCase;
    private final AssessMatchIntegrityUseCase assessMatchIntegrityUseCase;

    public MatchAdminController(SyncMatchStatisticsUseCase syncMatchStatisticsUseCase, SyncMatchUseCase syncMatchUseCase, SyncOddsUseCase syncOddsUseCase, AssessMatchIntegrityUseCase assessMatchIntegrityUseCase) {
        this.syncMatchStatisticsUseCase = syncMatchStatisticsUseCase;
        this.syncMatchUseCase = syncMatchUseCase;
        this.syncOddsUseCase = syncOddsUseCase;
        this.assessMatchIntegrityUseCase = assessMatchIntegrityUseCase;
    }

    @PostMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> syncByExternalId(@RequestParam Long externalId) {
        syncMatchUseCase.syncByExternalId(externalId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/sync-league", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MatchResponse>> syncByLeagueAndSeason(
            @RequestParam Long leagueExternalId, @RequestParam int season) {
        syncMatchUseCase.syncByLeagueAndSeason(leagueExternalId, season);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/sync-batch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> syncByExternalIds(@RequestParam List<Long> externalIds) {
        syncMatchUseCase.syncByExternalIds(externalIds);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/sync-statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> syncStatisticsByMatchExternalId(@RequestParam Long matchExternalId) {
        syncMatchStatisticsUseCase.syncByMatchExternalId(matchExternalId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/sync-odds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> syncOddsByMatchExternalId(@RequestParam Long matchExternalId) {
        syncOddsUseCase.syncByMatchExternalId(matchExternalId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/{id}/assess-integrity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntegrityAssessmentResponse> assessIntegrity(@PathVariable UUID id) {
        return ResponseEntity.ok(IntegrityAssessmentResponse.from(assessMatchIntegrityUseCase.execute(id)));
    }
}
