alter table tb_users add email varchar(255);
alter table tb_users add constraint uk_email unique (email);
alter table tb_users alter column password drop not null;

alter table tb_users add column auth_provider varchar(50);
update tb_users set auth_provider = 'LOCAL' where auth_provider is null;
alter table tb_users alter column auth_provider set not null;