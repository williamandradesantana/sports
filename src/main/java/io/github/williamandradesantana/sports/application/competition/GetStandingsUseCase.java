package io.github.williamandradesantana.sports.application.competition;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.competition.StandingRepository;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;

import java.util.List;
import java.util.UUID;

public class GetStandingsUseCase {

    private final StandingRepository standingRepository;
    private final TeamRepository teamRepository;

    public GetStandingsUseCase(StandingRepository standingRepository, TeamRepository teamRepository) {
        this.standingRepository = standingRepository;
        this.teamRepository = teamRepository;
    }

    public List<StandingWithTeam> execute(UUID seasonId) {
        return standingRepository.findBySeasonId(seasonId).stream()
            .sorted((a,  b) -> Integer.compare(a.getRank(), b.getRank()))
            .map(standing -> {
                Team team = teamRepository.findById(standing.getTeamId())
                        .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + standing.getTeamId()));
                return new StandingWithTeam(standing, team);
            }).toList();
    }
}
