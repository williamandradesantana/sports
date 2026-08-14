package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.domain.match.MatchStatus;

import java.util.Map;

public class MatchStatusMapping {

    public static final Map<String, MatchStatus> STATUS_MAPPING = Map.ofEntries(
            Map.entry("TBD", MatchStatus.TO_BE_DEFINED),
            Map.entry("NS", MatchStatus.SCHEDULED),
            Map.entry("1H", MatchStatus.LIVE),
            Map.entry("HT", MatchStatus.LIVE),
            Map.entry("2H", MatchStatus.LIVE),
            Map.entry("ET", MatchStatus.LIVE),
            Map.entry("BT", MatchStatus.LIVE),
            Map.entry("P", MatchStatus.LIVE),
            Map.entry("LIVE", MatchStatus.LIVE),
            Map.entry("FT", MatchStatus.FINISHED),
            Map.entry("AET", MatchStatus.FINISHED),
            Map.entry("PEN", MatchStatus.FINISHED),
            Map.entry("WO", MatchStatus.FINISHED),
            Map.entry("AWD", MatchStatus.FINISHED),
            Map.entry("PST", MatchStatus.POSTPONED),
            Map.entry("CANC", MatchStatus.CANCELLED),
            Map.entry("ABD", MatchStatus.SUSPENDED),
            Map.entry("SUSP", MatchStatus.SUSPENDED),
            Map.entry("INT", MatchStatus.SUSPENDED)
    );
}
