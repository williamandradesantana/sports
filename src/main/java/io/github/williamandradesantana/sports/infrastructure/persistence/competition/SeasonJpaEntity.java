package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_seasons")
public class SeasonJpaEntity {

    @Id
    private UUID id;

    @Column(name = "league_id", nullable = false)
    private UUID leagueId;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "current", nullable = false)
    private boolean current;

    public SeasonJpaEntity(){}

    public SeasonJpaEntity(UUID id, UUID leagueId, Integer year, LocalDate startDate, LocalDate endDate, boolean current) {
        this.id = id;
        this.leagueId = leagueId;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.current = current;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(UUID leagueId) {
        this.leagueId = leagueId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SeasonJpaEntity that = (SeasonJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
