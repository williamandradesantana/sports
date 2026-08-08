package io.github.williamandradesantana.sports.application.league;

import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;

import java.util.List;
import java.util.UUID;

public class SyncLeagueUseCase {

    private final LeagueProvider leagueProvider;
    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;

    public SyncLeagueUseCase(LeagueProvider leagueProvider, LeagueRepository leagueRepository, SeasonRepository seasonRepository) {
        this.leagueProvider = leagueProvider;
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
    }

    public void syncByExternalId(Long externalId) {
        List<ExternalLeagueData> externalLeagues = leagueProvider.fetchLeagueByExternalId(externalId);
        externalLeagues.forEach(this::syncLeague);
    }

    private void syncLeague(ExternalLeagueData externalLeague) {
        League league = leagueRepository.findByExternalId(externalLeague.externalId())
                .map(existing -> updateExistingLeague(existing, externalLeague))
                .orElseGet(() -> createNewLeague(externalLeague));

        leagueRepository.save(league);
        externalLeague.seasons().forEach(externalSeason -> syncSeason(league.getId(), externalSeason));
    }

    private League updateExistingLeague(League league, ExternalLeagueData externalLeague) {
        league.updateFromExternalSource(
            externalLeague.name(),
            externalLeague.type(),
            externalLeague.logoUrl(),
            externalLeague.country()
        );
        return league;
    }

    private League createNewLeague(ExternalLeagueData externalLeague) {
        return new League(
            UUID.randomUUID(),
            externalLeague.externalId(),
            externalLeague.name(),
            externalLeague.type(),
            externalLeague.logoUrl(),
            externalLeague.country()
        );
    }

    private void syncSeason(UUID leagueId, ExternalSeasonData externalSeason) {
        Season season = seasonRepository.findByLeagueIdAndYear(leagueId, externalSeason.year())
                .map(existing -> updateExistingSeason(existing, externalSeason))
                .orElseGet(() -> createNewSeason(leagueId, externalSeason));

        seasonRepository.save(season);
    }

    private Season updateExistingSeason(Season season, ExternalSeasonData externalSeason) {
        if (externalSeason.current()) {
            season.markAsCurrent();
        }
        else {
            season.markAsFinished();
        }
        return season;
    }

    private Season createNewSeason(UUID leagueId, ExternalSeasonData externalSeason) {
        return new Season(
            UUID.randomUUID(),
            leagueId,
            externalSeason.year(),
            externalSeason.startDate(),
            externalSeason.endDate(),
            externalSeason.current()
        );
    }
}
