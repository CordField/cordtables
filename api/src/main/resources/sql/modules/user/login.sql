CREATE OR REPLACE PROCEDURE admin.login(
    in p_email VARCHAR(255),
    in p_token varchar(64),
    inout error_type varchar(32),
    inout user_id varchar(32)
)
LANGUAGE PLPGSQL
AS $$
DECLARE
    vToken varchar(512);
BEGIN
    SELECT id
    FROM admin.users
    INTO user_id
    WHERE email = p_email;

    if user_id is not null then
      insert into admin.tokens ("token", "person")
      values (p_token, user_id::varchar);

      error_type = 'NoError';

    else
        error_type = 'BadCredentials';
    end if;

END; $$;


