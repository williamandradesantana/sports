create table tb_access_logs (
    id uuid not null primary key,
    user_id uuid,
    username varchar(255) not null,
    provider varchar(50) not null,
    ip_address varchar(45),
    user_agent varchar(500),
    success boolean not null,
    failure_reason varchar(255),
    occurred_at timestamptz not null default now()
);

create index idx_access_logs_username on tb_access_logs(username);
create index idx_access_logs_occurred_at on tb_access_logs(occurred_at);