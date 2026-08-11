create table tb_venues (
    id uuid not null primary key,
    external_id bigint not null,
    name varchar(255) not null,
    address varchar(255),
    city varchar(255),
    capacity integer,
    surface varchar(50),
    image_url varchar(500),
    constraint uk_venues_external_id unique (external_id)
);