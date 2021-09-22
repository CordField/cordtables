CREATE OR REPLACE PROCEDURE login(
    in p_email VARCHAR(255),
    in p_token varchar(64),
    inout error_type varchar(32)
)
LANGUAGE PLPGSQL
AS $$
DECLARE
    vPersonId INT;
    vToken varchar(512);
BEGIN
    SELECT person
    FROM users
    INTO vPersonId
    WHERE email = p_email;

    if vPersonId is not null then
      insert into public.tokens ("token", "person")
      values (p_token, vPersonId);

      error_type = 'NoError';

    else
        error_type = 'BadCredentials';
    end if;

END; $$;