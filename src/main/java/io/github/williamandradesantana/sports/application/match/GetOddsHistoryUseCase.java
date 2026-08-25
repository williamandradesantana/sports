package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.Odds;
import io.github.williamandradesantana.sports.domain.match.OddsRepository;

import java.util.List;
import java.util.UUID;

public class GetOddsHistoryUseCase {

    private final MatchRepository matchRepository;
    private final OddsRepository oddsRepository;

    public GetOddsHistoryUseCase(MatchRepository matchRepository, OddsRepository oddsRepository) {
        this.matchRepository = matchRepository;
        this.oddsRepository = oddsRepository;
    }

    public List<Odds> execute(UUID matchId) {
        matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));

        return oddsRepository.findByMatchId(matchId);
    }
}
