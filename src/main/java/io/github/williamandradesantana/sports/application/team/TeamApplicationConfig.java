package io.github.williamandradesantana.sports.application.team;

import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamApplicationConfig {

    @Bean
    public SyncTeamUseCase syncTeamUseCase(
            TeamProvider teamProvider, TeamRepository teamRepository, VenueRepository venueRepository
    ) {
        return new SyncTeamUseCase(teamProvider, teamRepository, venueRepository);
    }
}
