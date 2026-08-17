package io.github.williamandradesantana.sports.interfaces.match;

import io.github.williamandradesantana.sports.application.match.*;
import io.github.williamandradesantana.sports.interfaces.match.dto.MatchDetailResponse;
import io.github.williamandradesantana.sports.interfaces.match.dto.MatchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final GetMatchStatisticsUseCase getMatchStatisticsUseCase;
    private final GetMatchDetailsUseCase getMatchDetailsUseCase;
    private final GetMatchesByTeamUseCase getMatchesByTeamUseCase;

    public MatchController(GetMatchStatisticsUseCase getMatchStatisticsUseCase, GetMatchDetailsUseCase getMatchDetailsUseCase, GetMatchesByTeamUseCase getMatchesByTeamUseCase) {
        this.getMatchStatisticsUseCase = getMatchStatisticsUseCase;
        this.getMatchDetailsUseCase = getMatchDetailsUseCase;
        this.getMatchesByTeamUseCase = getMatchesByTeamUseCase;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MatchDetailResponse> getById(@PathVariable("id") UUID id) {
        MatchDetails details = getMatchDetailsUseCase.execute(id);
        MatchStatisticsPair statistics = getMatchStatisticsUseCase.execute(id);
        return ResponseEntity.ok(MatchDetailResponse.from(details, statistics));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MatchResponse>> listByTeam(
            @RequestParam UUID teamId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "0") String direction
    ) {
        var sortedDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortedDirection, "matchDate"));

        Page<MatchResponse> response = getMatchesByTeamUseCase.execute(pageable, teamId).map(MatchResponse::from);
        return ResponseEntity.ok(response);
    }
}
