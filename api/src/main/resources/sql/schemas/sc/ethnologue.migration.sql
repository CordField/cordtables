CREATE OR REPLACE PROCEDURE sc_migrate_ethnologue(
    in code VARCHAR(3),
    in name varchar(64),
    in population: int,
    in provisionalCode: varchar(16),
    in sensitivity varchar(16),
    inout error_type varchar(32)
)
LANGUAGE PLPGSQL
AS $$
DECLARE
    vPersonId INT;
    vToken varchar(512);
BEGIN
    SELECT person
    FROM common.users
    INTO vPersonId
    WHERE email = p_email;

    if vPersonId is not null then
      insert into admin.tokens ("token", "person")
      values (p_token, vPersonId);

      error_type = 'NoError';

    else
        error_type = 'BadCredentials';
    end if;

END; $$;