package io.github.williamandradesantana.sports.application.team;

import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamApplicationConfig {

    @Bean
    public GetTeamDetailsUseCase getTeamDetailsUseCase(TeamRepository teamRepository, VenueRepository venueRepository) {
        return new GetTeamDetailsUseCase(teamRepository, venueRepository);
    }

    @Bean
    public ListTeamUseCase listTeamUseCase(TeamRepository teamRepository) {
        return new ListTeamUseCase(teamRepository);
    }

    @Bean
    public SyncTeamUseCase syncTeamUseCase(
            TeamProvider teamProvider, TeamRepository teamRepository, VenueRepository venueRepository
    ) {
        return new SyncTeamUseCase(teamProvider, teamRepository, venueRepository);
    }
}
