package io.github.williamandradesantana.sports.interfaces.competition;

import io.github.williamandradesantana.sports.application.competition.GetStandingsUseCase;
import io.github.williamandradesantana.sports.interfaces.competition.dto.StandingResponse;
import io.github.williamandradesantana.sports.interfaces.shared.dto.ListResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/standings")
public class StandingController {

    private final GetStandingsUseCase getStandingsUseCase;

    public StandingController(GetStandingsUseCase getStandingsUseCase) {
        this.getStandingsUseCase = getStandingsUseCase;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListResponse<StandingResponse>> getBySeasonId(@RequestParam UUID seasonId) {
        List<StandingResponse> response = getStandingsUseCase.execute(seasonId).stream()
                .map(sw -> StandingResponse.from(sw.standing(), sw.team()))
                .toList();
        return ResponseEntity.ok(ListResponse.of(response));
    }
}
