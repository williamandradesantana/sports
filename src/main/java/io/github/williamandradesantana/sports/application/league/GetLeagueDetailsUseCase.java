package io.github.williamandradesantana.sports.application.league;

import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public class GetLeagueDetailsUseCase {

    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;

    public GetLeagueDetailsUseCase(LeagueRepository leagueRepository, SeasonRepository seasonRepository) {
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
    }

    public LeagueDetails execute(UUID id) {
        League league = leagueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("League not found: " + id));

        List<Season> seasons = seasonRepository.findAllLeagueId(id);
        return new LeagueDetails(league, seasons);
    }
}
