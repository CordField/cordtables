CREATE OR REPLACE PROCEDURE admin.register(
  IN p_email VARCHAR(255),
  IN p_password VARCHAR(50),
  in p_token varchar(64),
  INOUT error_type VARCHAR(32), 
  INOUT user_id integer
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vPersonId int;
  vEmail VARCHAR(255);
  vToken varchar(512);
BEGIN
  -- check to see if the email exists, if not continue
  SELECT email, id
  FROM admin.users
  INTO vEmail
  WHERE users.email = p_email;

  if vEmail is null then

    insert into admin.people("sensitivity_clearance")
    values ('Low')
    returning id
    into vPersonId;

    insert into admin.tokens ("token", "person")
    values (p_token, vPersonId);

    insert into admin.users(person, email, password, created_by, modified_by, owning_person, owning_group)
    values (vPersonId, p_email, p_password, 1, 1, 1, 1)
    returning id 
    into user_id;

    error_type := 'NoError';

  else
      error_type := 'DuplicateEmail';
  end if;

END; $$;