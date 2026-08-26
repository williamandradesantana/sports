package io.github.williamandradesantana.sports.domain.integrity;

import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchStatistics;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IntegrityScoringService {

    private static final int DOMINANT_TEAM_LOST_POINTS = 35;
    private static final int POSSESSION_WITHOUT_CONVERSION_POINTS = 30;
    private static final int INEFFICIENT_VICTORY_POINTS = 25;

    public IntegrityAssessment assess(Match match, MatchStatistics homeStats, MatchStatistics awayStats) {
        List<IntegrityFactor> factors = new ArrayList<>();

        checkDominantTeamLost(match, homeStats, awayStats).ifPresent(factors::add);
        checkPossessionWithoutConversion(match, homeStats, awayStats).ifPresent(factors::add);
        checkInefficientVictory(match, homeStats, awayStats).ifPresent(factors::add);

        int totalScore = Math.min(100, factors.stream().mapToInt(IntegrityFactor::points).sum());

        return new IntegrityAssessment(UUID.randomUUID(), match.getId(), totalScore, factors, OffsetDateTime.now());
    }

    private Optional<IntegrityFactor> checkDominantTeamLost(Match match, MatchStatistics home, MatchStatistics away) {
        Optional<UUID> winner = match.getWinnerId();
        if (winner.isEmpty()) return Optional.empty();

        boolean homeDominatedShots = isDominant(home.getTotalShots(), away.getTotalShots())
                && isDominant(home.getShotsOnGoal(), away.getShotsOnGoal());
        boolean awayDominatedShots = isDominant(away.getTotalShots(), home.getTotalShots())
                && isDominant(away.getShotsOnGoal(), home.getShotsOnGoal());

        boolean homeLostDespiteDominance = homeDominatedShots && winner.get().equals(match.getAwayTeamId());
        boolean awayLostDespiteDominance = awayDominatedShots && winner.get().equals(match.getHomeTeamId());

        if (homeLostDespiteDominance || awayLostDespiteDominance) {
            return Optional.of(new IntegrityFactor(
                    "DOMINANT_TEAM_LOST",
                    "The team with significantly more shots (total and on target) lost the match",
                    DOMINANT_TEAM_LOST_POINTS
            ));
        }
        return Optional.empty();
    }

    private Optional<IntegrityFactor> checkPossessionWithoutConversion(Match match, MatchStatistics home,
                                                                       MatchStatistics away) {
        boolean homeCase = exceedsPossessionThreshold(home) && scoredZeroWhileConceding(match.getHomeGoals(), match.getAwayGoals());
        boolean awayCase = exceedsPossessionThreshold(away) && scoredZeroWhileConceding(match.getAwayGoals(), match.getHomeGoals());

        if (homeCase || awayCase) {
            return Optional.of(new IntegrityFactor(
                    "POSSESSION_WITHOUT_CONVERSION",
                    "High ball possession (>65%) with zero goals while conceding two or more",
                    POSSESSION_WITHOUT_CONVERSION_POINTS
            ));
        }
        return Optional.empty();
    }

    private Optional<IntegrityFactor> checkInefficientVictory(Match match, MatchStatistics home, MatchStatistics away) {
        Optional<UUID> winner = match.getWinnerId();
        if (winner.isEmpty()) return Optional.empty();

        int goalMargin = Math.abs(match.getHomeGoals().orElse(0) - match.getAwayGoals().orElse(0));
        if (goalMargin < 2) return Optional.empty();

        boolean homeWonInefficiently = winner.get().equals(match.getHomeTeamId())
                && isInefficient(home.getShotsOnGoal(), away.getShotsOnGoal());
        boolean awayWonInefficiently = winner.get().equals(match.getAwayTeamId())
                && isInefficient(away.getShotsOnGoal(), home.getShotsOnGoal());

        if (homeWonInefficiently || awayWonInefficiently) {
            return Optional.of(new IntegrityFactor(
                    "INEFFICIENT_VICTORY",
                    "Large goal margin victory despite very few shots on target compared to the opponent",
                    INEFFICIENT_VICTORY_POINTS
            ));
        }
        return Optional.empty();
    }

    private boolean isDominant(Optional<Integer> value, Optional<Integer> opponentValue) {
        if (value.isEmpty() || opponentValue.isEmpty()) return false;
        return value.get() >= opponentValue.get() * 2;
    }

    private boolean exceedsPossessionThreshold(MatchStatistics stats) {
        return stats.getBallPossessionPercentage().map(p -> p > 65).orElse(false);
    }

    private boolean scoredZeroWhileConceding(Optional<Integer> scored, Optional<Integer> conceded) {
        return scored.map(s -> s == 0).orElse(false) && conceded.map(c -> c >= 2).orElse(false);
    }

    private boolean isInefficient(Optional<Integer> winnerShotsOnGoal, Optional<Integer> loserShotsOnGoal) {
        if (winnerShotsOnGoal.isEmpty() || loserShotsOnGoal.isEmpty()) return false;
        return winnerShotsOnGoal.get() <= 2 && loserShotsOnGoal.get() >= 8;
    }
}
