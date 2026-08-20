package io.github.williamandradesantana.sports.application.competition;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.competition.Standing;
import io.github.williamandradesantana.sports.domain.competition.StandingRepository;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;

import java.util.List;
import java.util.UUID;

public class SyncStandingsUseCase {

    private final StandingProvider standingProvider;
    private final StandingRepository standingRepository;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;

    public SyncStandingsUseCase(StandingProvider standingProvider, StandingRepository standingRepository, TeamRepository teamRepository, SeasonRepository seasonRepository, LeagueRepository leagueRepository) {
        this.standingProvider = standingProvider;
        this.standingRepository = standingRepository;
        this.teamRepository = teamRepository;
        this.seasonRepository = seasonRepository;
        this.leagueRepository = leagueRepository;
    }

    public List<Standing> syncByLeagueAndSeason(Long leagueExternalId, int season) {
        League league = leagueRepository.findByExternalId(leagueExternalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "League not synced yet: externalId=" + leagueExternalId));

        Season seasonEntity = seasonRepository.findByLeagueIdAndYear(league.getId(), season)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Season not synced yet: league=" + leagueExternalId + ", year=" + season));

        List<ExternalStandingData> externalData = standingProvider.fetchByLeagueAndSeason(leagueExternalId, season);

        return externalData.stream()
                .map(data -> syncStanding(seasonEntity.getId(), data)).toList();
    }

    private Standing syncStanding(UUID seasonId, ExternalStandingData data) {
        Team team = teamRepository.findByExternalId(data.teamExternalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Team not synced yet: externalId=" + data.teamExternalId()));

        Standing standing = standingRepository.findBySeasonIdAndTeamId(seasonId, team.getId())
                .map(existing -> updateExisting(existing, data))
                .orElseGet(() -> createNew(seasonId, team.getId(), data));

        standingRepository.save(standing);
        return standing;
    }

    private Standing updateExisting(Standing standing, ExternalStandingData data) {
        standing.updateFromExternalSource(data.rank(), data.points(), data.groupName(), data.form(),
                data.trend(), data.description(), data.overall(), data.home(), data.away(), data.lastUpdatedAt());
        return standing;
    }

    private Standing createNew(UUID seasonId, UUID teamId, ExternalStandingData data) {
        return new Standing(
                UUID.randomUUID(), seasonId, teamId, data.rank(), data.points(), data.groupName(), data.form(),
                data.trend(), data.description(), data.overall(), data.home(), data.away(), data.lastUpdatedAt()
        );
    }
}
