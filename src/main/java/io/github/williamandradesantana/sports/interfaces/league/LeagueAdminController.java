package io.github.williamandradesantana.sports.interfaces.league;

import io.github.williamandradesantana.sports.application.league.SyncLeagueUseCase;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.interfaces.league.dto.LeagueResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/leagues")
@PreAuthorize("hasAuthority('ADMIN')")
public class LeagueAdminController {

    private final SyncLeagueUseCase syncLeagueUseCase;

    public LeagueAdminController(SyncLeagueUseCase syncLeagueUseCase) {
        this.syncLeagueUseCase = syncLeagueUseCase;
    }

    @PostMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LeagueResponse> sync(@RequestParam Long externalId) {
        League league = syncLeagueUseCase.syncByExternalId(externalId);
        return ResponseEntity.status(HttpStatus.OK).body(LeagueResponse.from(league));
    }
}
