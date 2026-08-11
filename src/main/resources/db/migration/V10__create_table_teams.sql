create table tb_teams (
   id uuid not null primary key,
   external_id bigint not null,
   name varchar(255) not null,
   code varchar(10),
   country_name varchar(255),
   founded integer,
   national boolean not null default false,
   logo_url varchar(500),
   venue_id uuid,
   constraint uk_teams_external_id unique (external_id),
   constraint fk_teams_venue foreign key (venue_id) references tb_venues(id)
);