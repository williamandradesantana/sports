package io.github.williamandradesantana.sports.domain.competition;

import io.github.williamandradesantana.sports.domain.competition.exceptions.InvalidStandingRecordException;

public record StandingRecord(int played, int win, int draw, int lose, int goalsFor, int goalsAgainst) {
    public StandingRecord {
        if (played < 0 || win < 0 || draw < 0 || lose < 0)
            throw new InvalidStandingRecordException("Played/win/draw/lose cannot be negative");
    }

    public int goalDifference() {
        return goalsFor - goalsAgainst;
    }
}
