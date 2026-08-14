create table tb_matches (
    id uuid not null primary key,
    external_id bigint not null,
    league_id uuid not null,
    season_id uuid not null,
    home_team_id uuid not null,
    away_team_id uuid not null,
    venue_id uuid,
    match_date timestamptz not null,
    status varchar(30) not null
        check (status in ('SCHEDULED','LIVE','FINISHED','POSTPONED','CANCELLED','SUSPENDED','TO_BE_DEFINED')),
    home_goals integer,
    away_goals integer,
    round varchar(255),
    referee varchar(255),
    constraint uk_matches_external_id unique (external_id),
    constraint fk_matches_league foreign key (league_id) references tb_leagues(id),
    constraint fk_matches_season foreign key (season_id) references tb_seasons(id),
    constraint fk_matches_home_team foreign key (home_team_id) references tb_teams(id),
    constraint fk_matches_away_team foreign key (away_team_id) references tb_teams(id),
    constraint fk_matches_venue foreign key (venue_id) references tb_venues(id)
);