package io.github.williamandradesantana.sports.interfaces.competition;

import io.github.williamandradesantana.sports.application.competition.SyncStandingsUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/standings")
@PreAuthorize("hasAuthority('ADMIN')")
public class StandingAdminController {

    private final SyncStandingsUseCase syncStandingsUseCase;

    public StandingAdminController(SyncStandingsUseCase syncStandingsUseCase) {
        this.syncStandingsUseCase = syncStandingsUseCase;
    }

    @PostMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sync(@RequestParam Long leagueExternalId, @RequestParam int season) {
        syncStandingsUseCase.syncByLeagueAndSeason(leagueExternalId, season);
        return ResponseEntity.accepted().build();
    }
}