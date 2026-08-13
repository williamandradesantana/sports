package io.github.williamandradesantana.sports.interfaces.team;

import io.github.williamandradesantana.sports.application.team.SyncTeamUseCase;
import io.github.williamandradesantana.sports.interfaces.team.dto.TeamResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/teams")
@PreAuthorize("hasAuthority('ADMIN')")
public class TeamAdminController {

    private final SyncTeamUseCase syncTeamUseCase;

    public TeamAdminController(SyncTeamUseCase syncTeamUseCase) {
        this.syncTeamUseCase = syncTeamUseCase;
    }

    @PostMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamResponse> syncByExternalId(@RequestParam Long externalId) {
        return ResponseEntity.ok(TeamResponse.from(syncTeamUseCase.syncByExternalId(externalId)));
    }

    @PostMapping(value = "/sync-league", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TeamResponse>> syncByLeagueAndSeason(
            @RequestParam Long leagueExternalId, @RequestParam int season
    ) {
       List<TeamResponse> response = syncTeamUseCase.syncByLeagueAndSeason(leagueExternalId, season)
               .stream().map(TeamResponse::from).toList();
       return ResponseEntity.ok(response);
    }
}
