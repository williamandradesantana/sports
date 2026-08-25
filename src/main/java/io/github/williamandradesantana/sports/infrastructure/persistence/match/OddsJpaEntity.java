package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_odds")
public class OddsJpaEntity {

    @Id
    private UUID id;

    @Column(name = "match_id", nullable = false)
    private UUID matchId;

    @Column(name = "bookmaker_external_id", nullable = false)
    private Long bookmakerExternalId;

    @Column(name = "bookmaker_name", nullable = false)
    private String bookmakerName;

    @Column(name = "captured_at", nullable = false)
    private OffsetDateTime capturedAt;

    @Column(name = "home_win_odd")
    private BigDecimal homeWinOdd;

    @Column(name = "draw_odd")
    private BigDecimal drawOdd;

    @Column(name = "away_win_odd")
    private BigDecimal awayWinOdd;

    @Column(name = "over_goals_odd")
    private BigDecimal overGoalsOdd;

    @Column(name = "under_goals_odd")
    private BigDecimal underGoalsOdd;

    @Column(name = "both_teams_score_yes_odd")
    private BigDecimal bothTeamsScoreYesOdd;

    @Column(name = "both_teams_score_no_odd")
    private BigDecimal bothTeamsScoreNoOdd;

    protected OddsJpaEntity(){}

    public OddsJpaEntity(UUID id, UUID matchId, Long bookmakerExternalId, String bookmakerName, OffsetDateTime capturedAt, BigDecimal homeWinOdd, BigDecimal drawOdd, BigDecimal awayWinOdd, BigDecimal overGoalsOdd, BigDecimal underGoalsOdd, BigDecimal bothTeamsScoreYesOdd, BigDecimal bothTeamsScoreNoOdd) {
        this.id = id;
        this.matchId = matchId;
        this.bookmakerExternalId = bookmakerExternalId;
        this.bookmakerName = bookmakerName;
        this.capturedAt = capturedAt;
        this.homeWinOdd = homeWinOdd;
        this.drawOdd = drawOdd;
        this.awayWinOdd = awayWinOdd;
        this.overGoalsOdd = overGoalsOdd;
        this.underGoalsOdd = underGoalsOdd;
        this.bothTeamsScoreYesOdd = bothTeamsScoreYesOdd;
        this.bothTeamsScoreNoOdd = bothTeamsScoreNoOdd;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    public Long getBookmakerExternalId() {
        return bookmakerExternalId;
    }

    public void setBookmakerExternalId(Long bookmakerExternalId) {
        this.bookmakerExternalId = bookmakerExternalId;
    }

    public String getBookmakerName() {
        return bookmakerName;
    }

    public void setBookmakerName(String bookmakerName) {
        this.bookmakerName = bookmakerName;
    }

    public OffsetDateTime getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(OffsetDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }

    public BigDecimal getHomeWinOdd() {
        return homeWinOdd;
    }

    public void setHomeWinOdd(BigDecimal homeWinOdd) {
        this.homeWinOdd = homeWinOdd;
    }

    public BigDecimal getDrawOdd() {
        return drawOdd;
    }

    public void setDrawOdd(BigDecimal drawOdd) {
        this.drawOdd = drawOdd;
    }

    public BigDecimal getAwayWinOdd() {
        return awayWinOdd;
    }

    public void setAwayWinOdd(BigDecimal awayWinOdd) {
        this.awayWinOdd = awayWinOdd;
    }

    public BigDecimal getOverGoalsOdd() {
        return overGoalsOdd;
    }

    public void setOverGoalsOdd(BigDecimal overGoalsOdd) {
        this.overGoalsOdd = overGoalsOdd;
    }

    public BigDecimal getUnderGoalsOdd() {
        return underGoalsOdd;
    }

    public void setUnderGoalsOdd(BigDecimal underGoalsOdd) {
        this.underGoalsOdd = underGoalsOdd;
    }

    public BigDecimal getBothTeamsScoreYesOdd() {
        return bothTeamsScoreYesOdd;
    }

    public void setBothTeamsScoreYesOdd(BigDecimal bothTeamsScoreYesOdd) {
        this.bothTeamsScoreYesOdd = bothTeamsScoreYesOdd;
    }

    public BigDecimal getBothTeamsScoreNoOdd() {
        return bothTeamsScoreNoOdd;
    }

    public void setBothTeamsScoreNoOdd(BigDecimal bothTeamsScoreNoOdd) {
        this.bothTeamsScoreNoOdd = bothTeamsScoreNoOdd;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        OddsJpaEntity that = (OddsJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
