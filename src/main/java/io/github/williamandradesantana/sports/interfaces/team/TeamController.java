package io.github.williamandradesantana.sports.interfaces.team;

import io.github.williamandradesantana.sports.application.team.GetTeamDetailsUseCase;
import io.github.williamandradesantana.sports.application.team.ListTeamUseCase;
import io.github.williamandradesantana.sports.interfaces.team.dto.TeamDetailResponse;
import io.github.williamandradesantana.sports.interfaces.team.dto.TeamResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final ListTeamUseCase listTeamUseCase;
    private final GetTeamDetailsUseCase getTeamDetailsUseCase;

    public TeamController(ListTeamUseCase listTeamUseCase, GetTeamDetailsUseCase getTeamDetailsUseCase) {
        this.listTeamUseCase = listTeamUseCase;
        this.getTeamDetailsUseCase = getTeamDetailsUseCase;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TeamResponse>> list(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size,
        @RequestParam(value = "direction", defaultValue = "0") String direction
    ) {
       var sortedDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
       var pageable = PageRequest.of(page, size, Sort.by(sortedDirection, "name"));
       var teams = listTeamUseCase.execute(pageable).map(TeamResponse::from);
       return ResponseEntity.ok(teams);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDetailResponse> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(TeamDetailResponse.from(getTeamDetailsUseCase.execute(id)));
    }
}
