CREATE OR REPLACE PROCEDURE bootstrap(
  IN p_email VARCHAR(255),
  IN p_password VARCHAR(50)
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vPeopleCount int;
  vPersonId int;
  vOrgId int;
BEGIN
  select count(id)
  from public.people
  into vPeopleCount;

  if vPeopleCount = 0 then
    insert into public.people("sensitivity_clearance")
    values ('High')
    returning id
    into vPersonId;
  end if;

  select id
  from public.organizations
  into vOrgId
  where id = 0;

  if not found then
    insert into public.organizations("name", "sensitivity")
    values ('Seed Company', 'Low')
    returning id
    into vOrgId;
  end if;

  if vPeopleCount = 0 then
    insert into public.users("person", "owning_org", "email", "password")
    values (vPersonId, vOrgId, p_email, p_password);
  end if;

END; $$;