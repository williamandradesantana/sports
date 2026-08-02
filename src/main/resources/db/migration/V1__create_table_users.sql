create table tb_users (
    id uuid not null primary key,
    user_name varchar(255) default null,
    full_name varchar(255) default null,
    password varchar(255) default null,
    account_non_expired boolean not null default true,
    account_non_locked boolean not null default true,
    credentials_non_expired boolean not null default true,
    enabled boolean not null default true,
    constraint uk_user_name unique (user_name)
);