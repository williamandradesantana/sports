package io.github.williamandradesantana.sports.application.team;

import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListTeamUseCase {

    private final TeamRepository teamRepository;

    public ListTeamUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Page<Team> execute(Pageable pageable) {
        return teamRepository.findAll(pageable);
    }
}
