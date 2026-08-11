package io.github.williamandradesantana.sports.interfaces.league;

import io.github.williamandradesantana.sports.application.league.GetLeagueDetailsUseCase;
import io.github.williamandradesantana.sports.application.league.LeagueDetails;
import io.github.williamandradesantana.sports.application.league.ListLeagueUseCase;
import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.interfaces.competition.dto.SeasonResponse;
import io.github.williamandradesantana.sports.interfaces.league.dto.LeagueDetailsResponse;
import io.github.williamandradesantana.sports.interfaces.league.dto.LeagueResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leagues")
public class LeagueController {

    private final ListLeagueUseCase listLeagueUseCase;
    private final GetLeagueDetailsUseCase getLeagueDetailsUseCase;

    public LeagueController(ListLeagueUseCase listLeagueUseCase, GetLeagueDetailsUseCase getLeagueDetailsUseCase) {
        this.listLeagueUseCase = listLeagueUseCase;
        this.getLeagueDetailsUseCase = getLeagueDetailsUseCase;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<LeagueResponse>> list(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "0") String direction
    ) {
        var sortedDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortedDirection, "name"));
        var response = listLeagueUseCase.execute(pageable).map(LeagueResponse::from);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LeagueDetailsResponse> getById(@PathVariable("id") UUID id) {
        LeagueDetails leagueDetails = getLeagueDetailsUseCase.execute(id);
        List<SeasonResponse> seasons = leagueDetails.seasons().stream().map(SeasonResponse::from).toList();
        return ResponseEntity.ok(LeagueDetailsResponse.from(leagueDetails.league(), seasons));
    }
}
