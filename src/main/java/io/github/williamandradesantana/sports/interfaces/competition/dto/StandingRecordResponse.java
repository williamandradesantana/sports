package io.github.williamandradesantana.sports.interfaces.competition.dto;

import io.github.williamandradesantana.sports.domain.competition.StandingRecord;

public record StandingRecordResponse(int played, int win, int draw, int lose, int goalsFor, int goalsAgainst) {
    public static StandingRecordResponse from(StandingRecord record) {
        return new StandingRecordResponse(record.played(), record.win(), record.draw(), record.lose(),
                record.goalsFor(), record.goalsAgainst()
        );
    }
}
