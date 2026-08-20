package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StandingRecordEmbeddable {

    @Column(name = "played", nullable = false)
    private int played;

    @Column(name = "win", nullable = false)
    private int win;

    @Column(name = "draw", nullable = false)
    private int draw;

    @Column(name = "lose", nullable = false)
    private int lose;

    @Column(name = "goals_for", nullable = false)
    private int goalsFor;

    @Column(name = "goals_against", nullable = false)
    private int goalsAgainst;

    protected StandingRecordEmbeddable() {}

    public StandingRecordEmbeddable(int played, int win, int draw, int lose, int goalsFor, int goalsAgainst) {
        this.played = played;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
    }

    public int getPlayed() { return played; }
    public int getWin() { return win; }
    public int getDraw() { return draw; }
    public int getLose() { return lose; }
    public int getGoalsFor() { return goalsFor; }
    public int getGoalsAgainst() { return goalsAgainst; }
}
