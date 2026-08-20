create table tb_standings (
  id uuid not null primary key,
  season_id uuid not null,
  team_id uuid not null,
  rank integer not null,
  points integer not null,
  group_name varchar(255),
  form varchar(20),
  trend varchar(10) not null,
  description varchar(255),

  overall_played integer not null,
  overall_win integer not null,
  overall_draw integer not null,
  overall_lose integer not null,
  overall_goals_for integer not null,
  overall_goals_against integer not null,

  home_played integer not null,
  home_win integer not null,
  home_draw integer not null,
  home_lose integer not null,
  home_goals_for integer not null,
  home_goals_against integer not null,

  away_played integer not null,
  away_win integer not null,
  away_draw integer not null,
  away_lose integer not null,
  away_goals_for integer not null,
  away_goals_against integer not null,

  last_updated_at timestamptz not null,

  constraint uk_standings_season_team unique (season_id, team_id),
  constraint fk_standings_season foreign key (season_id) references tb_seasons(id),
  constraint fk_standings_team foreign key (team_id) references tb_teams(id)
);