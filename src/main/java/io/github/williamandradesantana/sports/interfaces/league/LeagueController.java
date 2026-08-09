package io.github.williamandradesantana.sports.interfaces.league;

import io.github.williamandradesantana.sports.application.league.SyncLeagueUseCase;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.interfaces.league.dto.LeagueResponse;
import io.github.williamandradesantana.sports.interfaces.shared.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leagues")
public class LeagueController {

    private final SyncLeagueUseCase syncLeagueUseCase;
    private final LeagueRepository leagueRepository;

    public LeagueController(SyncLeagueUseCase syncLeagueUseCase, LeagueRepository leagueRepository) {
        this.syncLeagueUseCase = syncLeagueUseCase;
        this.leagueRepository = leagueRepository;
    }

    @PostMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LeagueResponse> sync(@RequestParam Long externalId) {
        syncLeagueUseCase.syncByExternalId(externalId);

        League league = leagueRepository.findByExternalId(externalId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Sync completed but league was not found: externalId=" + externalId
            )
        );

        return ResponseEntity.status(HttpStatus.OK).body(toResponse(league));
    }

    private LeagueResponse toResponse(League league) {
        return new LeagueResponse(
            league.getId(), league.getExternalId(), league.getName(),
            league.getType().name(), league.getLogoUrl(), league.getCountry().name()
        );
    }
}
