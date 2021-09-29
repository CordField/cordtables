CREATE OR REPLACE PROCEDURE bootstrap(
  IN p_email VARCHAR(255),
  IN p_password VARCHAR(50),
  inout error_type varchar(32)
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vPeopleCount int;
  vPersonId int;
  vOrgId int;
  vAdminRoleId int;
BEGIN
  select count(id)
  from public.people
  into vPeopleCount;

  if vPeopleCount = 0 then
    -- people
    insert into public.people("sensitivity_clearance")
    values ('High')
    returning id
    into vPersonId;

    -- organization
    insert into public.organizations("name", "sensitivity")
    values ('Seed Company', 'Low')
    returning id
    into vOrgId;

    -- users
    insert into public.users("person", "owning_org", "email", "password")
    values (vPersonId, vOrgId, p_email, p_password);

    -- global roles
    insert into public.global_roles("name", "org")
    values ('Administrator', vOrgId)
    returning id
    into vAdminRoleId;

    -- global role memberships
    insert into public.global_role_memberships("global_role", person) values (vAdminRoleId, vPersonId);

    -- global role table grants
    insert into public.global_role_table_permissions("global_role", "table_permission", "table_name") values (vAdminRoleId, 'Create', 'public.people');
    insert into public.global_role_table_permissions("global_role", "table_permission", "table_name") values (vAdminRoleId, 'Delete', 'public.people');

    insert into public.global_role_table_permissions("global_role", "table_permission", "table_name") values (vAdminRoleId, 'Create', 'public.users');
    insert into public.global_role_table_permissions("global_role", "table_permission", "table_name") values (vAdminRoleId, 'Delete', 'public.users');

    insert into public.global_role_table_permissions("global_role", "table_permission", "table_name") values (vAdminRoleId, 'Create', 'public.organizations');
    insert into public.global_role_table_permissions("global_role", "table_permission", "table_name") values (vAdminRoleId, 'Delete', 'public.organizations');

    insert into public.global_role_table_permissions("global_role", "table_permission", "table_name") values (vAdminRoleId, 'Create', 'sc.languages_ex');
    insert into public.global_role_table_permissions("global_role", "table_permission", "table_name") values (vAdminRoleId, 'Delete', 'sc.languages_ex');

    -- global role column grants

    -- people
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'id', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'neo4j_id', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'about', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'created_at', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'created_by', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'modified_at', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'modified_by', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'phone', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'picture', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'primary_org', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'private_first_name', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'private_last_name', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'public_first_name', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'public_last_name', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'primary_location', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'private_full_name', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'public_full_name', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'sensitivity_clearance', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'time_zone', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'title', vAdminRoleId, 'public.people');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'status', vAdminRoleId, 'public.people');

    -- users
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'id', vAdminRoleId, 'public.users');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'person', vAdminRoleId, 'public.users');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'owning_org', vAdminRoleId, 'public.users');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'email', vAdminRoleId, 'public.users');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'password', vAdminRoleId, 'public.users');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'created_at', vAdminRoleId, 'public.users');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'created_by', vAdminRoleId, 'public.users');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'modified_at', vAdminRoleId, 'public.users');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'modified_by', vAdminRoleId, 'public.users');

    -- organizations
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'id', vAdminRoleId, 'public.organizations');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'neo4j_id', vAdminRoleId, 'public.organizations');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'created_at', vAdminRoleId, 'public.organizations');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'created_by', vAdminRoleId, 'public.organizations');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'modified_at', vAdminRoleId, 'public.organizations');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'modified_by', vAdminRoleId, 'public.organizations');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'name', vAdminRoleId, 'public.organizations');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'sensitivity', vAdminRoleId, 'public.organizations');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'primary_location', vAdminRoleId, 'public.organizations');

    -- sc.languages_ex
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'id', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'lang_name', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'lang_code', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'location', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'first_lang_population', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'population', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'egids_level', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'egids_value', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'least_reached_progress_jps_scale', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'least_reached_value', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'partner_interest', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'partner_interest_description', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'partner_interest_source', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'multi_lang_leverage', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'multi_lang_leverage_description', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'multi_lang_leverage_source', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'community_interest', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'community_interest_description', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'community_interest_source', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'community_interest_value', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'community_interest_scripture_description', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'community_interest_scripture_source', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'lwc_scripture_access', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'lwc_scripture_description', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'lwc_scripture_source', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'access_to_begin', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'access_to_begin_description', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'access_to_begin_source', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'suggested_strategies', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'comments', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'prioritization', vAdminRoleId, 'sc.languages_ex');
    insert into public.global_role_column_grants("access_level", "column_name", "global_role", "table_name") values ('Write', 'progress_bible', vAdminRoleId, 'sc.languages_ex');

  end if;

END; $$;