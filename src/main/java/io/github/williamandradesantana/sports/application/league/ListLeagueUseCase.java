package io.github.williamandradesantana.sports.application.league;

import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListLeagueUseCase {

    private final LeagueRepository leagueRepository;

    public ListLeagueUseCase(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    public Page<League> execute(Pageable pageable) {
        return leagueRepository.findAll(pageable);
    }
}
