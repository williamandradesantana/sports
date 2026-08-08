create table tb_seasons(
   id uuid not null primary key,
   league_id uuid not null,
   year integer not null,
   start_date date,
   end_date date,
   current boolean not null default false,

   constraint fk_seasons_league
       foreign key (league_id) references tb_leagues(id),
   constraint uk_seasons_league_year unique (league_id, year)
);