package io.github.williamandradesantana.sports.interfaces.match;

import io.github.williamandradesantana.sports.application.match.SyncMatchStatisticsUseCase;
import io.github.williamandradesantana.sports.application.match.SyncMatchUseCase;
import io.github.williamandradesantana.sports.interfaces.match.dto.MatchResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/matches")
@PreAuthorize("hasAuthority('ADMIN')")
public class MatchAdminController {

    private final SyncMatchStatisticsUseCase syncMatchStatisticsUseCase;
    private final SyncMatchUseCase syncMatchUseCase;

    public MatchAdminController(SyncMatchStatisticsUseCase syncMatchStatisticsUseCase, SyncMatchUseCase syncMatchUseCase) {
        this.syncMatchStatisticsUseCase = syncMatchStatisticsUseCase;
        this.syncMatchUseCase = syncMatchUseCase;
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
}
