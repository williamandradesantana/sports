INSERT INTO tb_users (
    id,
    user_name,
    full_name,
    password,
    email,
    auth_provider,
    account_non_expired,
    account_non_locked,
    credentials_non_expired,
    enabled
)
VALUES (
       gen_random_uuid(),
       'admin',
       'Administrator',
       '$2a$12$DoNW6.BFBrVBZlh0.4QBjuJtCULkwcEiWR98KslDUm2z.NLqZeFAK',
       'admin@example.com',
       'LOCAL',
       true,
       true,
       true,
       true
);

INSERT INTO tb_user_permissions (
    user_id,
    permission_id
)
SELECT
    u.id,
    p.id
FROM tb_users u
         JOIN tb_permissions p
              ON p.description = 'ADMIN'
WHERE u.user_name = 'admin';