CREATE OR REPLACE PROCEDURE admin.register(
  IN p_email VARCHAR(255),
  IN p_password VARCHAR(50),
  in p_token varchar(64),
  INOUT error_type VARCHAR(32), 
  INOUT user_id varchar(32)
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vAdminPersonId varchar(32);
  vAdminGroupId varchar(32);
  vPersonId varchar(32);
  vEmail VARCHAR(255);
  vToken varchar(512);
BEGIN
  -- check to see if the email exists, if not continue
  SELECT email, id
  FROM admin.users
  INTO vEmail
  WHERE users.email = p_email;

  if vEmail is null then

    select admin.people.id as id 				from admin.people
    inner join admin.role_memberships 	on admin.role_memberships.person = admin.people.id
    inner join admin.roles 				on admin.role_memberships.role = admin.roles.id
    where admin.roles.name = 'Administrator'
    order by admin.people.created_at asc
    limit 1
    into vAdminPersonId;

    select id
    from admin.groups
    where name = 'Administrators'
    into vAdminGroupId;

    insert into admin.people("sensitivity_clearance")
    values ('Low')
    returning id
    into vPersonId;

    insert into admin.tokens ("token", "person")
    values (p_token, vPersonId);

    insert into admin.users(id, email, password, created_by, modified_by, owning_person, owning_group)
    values (vPersonId, p_email, p_password, vAdminPersonId, vAdminPersonId, vAdminPersonId, vAdminGroupId)
    returning id 
    into user_id;

    error_type := 'NoError';

  else
      error_type := 'DuplicateEmail';
  end if;

END; $$;
