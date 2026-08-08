create table tb_leagues(
    id uuid not null primary key,
    external_id bigint not null,
    name varchar(255) not null,
    type varchar(50) not null,
    logo_url varchar(500),
    country_name varchar(255) not null,
    country_code varchar(10),
    country_flag_url varchar(500),
    constraint uk_leagues_external_id unique (external_id)
);