package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public class GetMatchesByTeamUseCase {

    private final MatchRepository matchRepository;
    private final GetMatchDetailsUseCase getMatchDetailsUseCase;

    public GetMatchesByTeamUseCase(MatchRepository matchRepository, GetMatchDetailsUseCase getMatchDetailsUseCase) {
        this.matchRepository = matchRepository;
        this.getMatchDetailsUseCase = getMatchDetailsUseCase;
    }

    public Page<MatchDetails> execute(Pageable pageable, UUID teamId) {
        return matchRepository.findByTeamId(pageable, teamId).map(getMatchDetailsUseCase::toDetails);
    }
}
