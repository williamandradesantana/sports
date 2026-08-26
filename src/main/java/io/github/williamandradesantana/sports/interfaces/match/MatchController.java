package io.github.williamandradesantana.sports.interfaces.match;

import io.github.williamandradesantana.sports.application.integrity.GetIntegrityAssessmentUseCase;
import io.github.williamandradesantana.sports.application.match.*;
import io.github.williamandradesantana.sports.interfaces.integrity.dto.IntegrityAssessmentResponse;
import io.github.williamandradesantana.sports.interfaces.match.dto.MatchDetailResponse;
import io.github.williamandradesantana.sports.interfaces.match.dto.MatchResponse;
import io.github.williamandradesantana.sports.interfaces.match.dto.OddsResponse;
import io.github.williamandradesantana.sports.interfaces.shared.dto.ListResponse;
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
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final GetMatchStatisticsUseCase getMatchStatisticsUseCase;
    private final GetMatchDetailsUseCase getMatchDetailsUseCase;
    private final GetMatchesByTeamUseCase getMatchesByTeamUseCase;
    private final GetOddsHistoryUseCase getOddsHistoryUseCase;
    private final GetIntegrityAssessmentUseCase getIntegrityAssessmentUseCase;

    public MatchController(GetMatchStatisticsUseCase getMatchStatisticsUseCase, GetMatchDetailsUseCase getMatchDetailsUseCase, GetMatchesByTeamUseCase getMatchesByTeamUseCase, GetOddsHistoryUseCase getOddsHistoryUseCase, GetIntegrityAssessmentUseCase getIntegrityAssessmentUseCase) {
        this.getMatchStatisticsUseCase = getMatchStatisticsUseCase;
        this.getMatchDetailsUseCase = getMatchDetailsUseCase;
        this.getMatchesByTeamUseCase = getMatchesByTeamUseCase;
        this.getOddsHistoryUseCase = getOddsHistoryUseCase;
        this.getIntegrityAssessmentUseCase = getIntegrityAssessmentUseCase;
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

    @GetMapping(value = "/{id}/odds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListResponse<OddsResponse>> getOddsHistory(@PathVariable UUID id) {
        List<OddsResponse> response = getOddsHistoryUseCase.execute(id)
                .stream().map(OddsResponse::from).toList();
        return ResponseEntity.ok(ListResponse.of(response));
    }

    @GetMapping(value = "/{id}/integrity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntegrityAssessmentResponse> getIntegrityAssessment(@PathVariable UUID id) {
        return ResponseEntity.ok(IntegrityAssessmentResponse.from(getIntegrityAssessmentUseCase.execute(id)));
    }
}
