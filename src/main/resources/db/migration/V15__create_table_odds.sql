create table tb_odds (
     id uuid not null primary key,
     match_id uuid not null,
     bookmaker_external_id bigint not null,
     bookmaker_name varchar(255) not null,
     captured_at timestamptz not null,
     home_win_odd numeric(6,2),
     draw_odd numeric(6,2),
     away_win_odd numeric(6,2),
     over_goals_odd numeric(6,2),
     under_goals_odd numeric(6,2),
     both_teams_score_yes_odd numeric(6,2),
     both_teams_score_no_odd numeric(6,2),
     constraint fk_odds_match foreign key (match_id) references tb_matches(id)
);

create index idx_odds_match_id on tb_odds(match_id);
create index idx_odds_match_id_captured_at on tb_odds(match_id, captured_at);