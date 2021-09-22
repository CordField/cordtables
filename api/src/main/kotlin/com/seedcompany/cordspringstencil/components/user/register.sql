CREATE OR REPLACE PROCEDURE register(
  IN p_email VARCHAR(255),
  IN p_password VARCHAR(50),
  in p_token varchar(64),
  INOUT error_type VARCHAR(32)
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vPersonId int;
  vEmail VARCHAR(255);

  vUserId INT;
  vToken varchar(512);
BEGIN
  -- check to see if the email exists, if not continue
  SELECT email
  FROM public.users
  INTO vEmail
  WHERE users.email = p_email;

  if vEmail is null then

    insert into public.people("sensitivity_clearance")
    values ('Low')
    returning id
    into vPersonId;

    insert into public.tokens ("token", "person")
    values (p_token, vPersonId);

    insert into public.users("person", "owning_org", "email", "password")
    values (vPersonId, 1, p_email, p_password);

    error_type := 'NoError';

  else
      error_type := 'DuplicateEmail';
  end if;

END; $$;