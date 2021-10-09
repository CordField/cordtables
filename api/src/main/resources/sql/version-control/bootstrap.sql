CREATE OR REPLACE PROCEDURE bootstrap(
  IN p_email VARCHAR(255),
  IN p_password VARCHAR(50),
  inout error_type varchar(32)
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vPeopleCount int; -- if this is a fresh db or not
  vPersonId int; -- the new root person id, some tables set defaults to 1
  vOrgId int; -- the default org for tables that set defaults to 1
  vAdminRoleId int;
  vAdminGroupId int;
BEGIN
  select count(id)
  from public.people
  into vPeopleCount;

  if vPeopleCount = 0 then
    -- people
    insert into public.people(sensitivity_clearance)
    values ('High')
    returning id
    into vPersonId;

    -- groups
    insert into public.groups(name, created_by, modified_by, owning_person)
    values ('Administrators', vPersonId, vPersonId, vPersonId)
    returning id
    into vAdminGroupId;

    -- organization
    insert into public.organizations(name, sensitivity, created_by, modified_by, owning_person, owning_group)
    values ('Seed Company', 'Low', vPersonId, vPersonId, vPersonId, vAdminGroupId)
    returning id
    into vOrgId;

    -- users
    insert into public.users(person, email, password, created_by, modified_by, owning_person, owning_group)
    values (vPersonId, p_email, p_password, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- global roles
    insert into public.global_roles(name, created_by, modified_by, owning_person, owning_group)
    values ('Administrator', vPersonId, vPersonId, vPersonId, vAdminGroupId)
    returning id
    into vAdminRoleId;

    -- global role memberships
    insert into public.global_role_memberships(global_role, person, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, vPersonId, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- global role table grants
    insert into public.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Create', 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Delete', 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into public.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Create', 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Delete', 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into public.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Create', 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Delete', 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into public.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Create', 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Delete', 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- global role column grants

    -- people
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'neo4j_id', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'about', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_at', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_by', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_at', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_by', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'phone', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'picture', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'primary_org', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'private_first_name', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'private_last_name', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'public_first_name', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'public_last_name', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'primary_location', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'private_full_name', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'public_full_name', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'sensitivity_clearance', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'time_zone', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'title', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'status', vAdminRoleId, 'public.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- users
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vAdminRoleId, 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'person', vAdminRoleId, 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'owning_org', vAdminRoleId, 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'email', vAdminRoleId, 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'password', vAdminRoleId, 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_at', vAdminRoleId, 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_by', vAdminRoleId, 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_at', vAdminRoleId, 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_by', vAdminRoleId, 'public.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- organizations
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vAdminRoleId, 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'neo4j_id', vAdminRoleId, 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_at', vAdminRoleId, 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_by', vAdminRoleId, 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_at', vAdminRoleId, 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_by', vAdminRoleId, 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'name', vAdminRoleId, 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'sensitivity', vAdminRoleId, 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'primary_location', vAdminRoleId, 'public.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- sc.languages_ex
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_name', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_code', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'location', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'first_lang_population', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'population', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'egids_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'egids_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'least_reached_progress_jps_scale', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'least_reached_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'partner_interest', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'partner_interest_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'partner_interest_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multi_lang_leverage', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multi_lang_leverage_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multi_lang_leverage_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'community_interest', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'community_interest_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'community_interest_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'community_interest_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'community_interest_scripture_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'community_interest_scripture_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lwc_scripture_access', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lwc_scripture_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lwc_scripture_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'access_to_begin', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'access_to_begin_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'access_to_begin_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'suggested_strategies', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'comments', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'prioritization', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'progress_bible', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- fake data for now
    insert into sc.languages_ex(language_name, iso, island, province, created_by, modified_by, owning_person, owning_group) values ('Spanglish', 'abc', 'US', 'Texarkana', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into sc.languages_ex(language_name, iso, island, province, created_by, modified_by, owning_person, owning_group) values ('Old Spanglish', 'abc', 'US', 'Texarkana', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into sc.languages_ex(language_name, iso, island, province, created_by, modified_by, owning_person, owning_group) values ('Pigin Spanglish', 'abc', 'US', 'Texarkana', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into sc.languages_ex(language_name, iso, island, province, created_by, modified_by, owning_person, owning_group) values ('Hick', 'abc', 'US', 'Texarkana', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into sc.languages_ex(language_name, iso, island, province, created_by, modified_by, owning_person, owning_group) values ('Hill Billy', 'abc', 'US', 'Texarkana', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into sc.languages_ex(language_name, iso, island, province, created_by, modified_by, owning_person, owning_group) values ('Creole', 'abc', 'US', 'Texarkana', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- group row access
    insert into public.group_row_access(group_id, table_name, row, created_by, modified_by, owning_person, owning_group) values (vAdminGroupId, 'sc.languages_ex', 1, vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.group_row_access(group_id, table_name, row, created_by, modified_by, owning_person, owning_group) values (vAdminGroupId, 'sc.languages_ex', 2, vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.group_row_access(group_id, table_name, row, created_by, modified_by, owning_person, owning_group) values (vAdminGroupId, 'sc.languages_ex', 3, vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.group_row_access(group_id, table_name, row, created_by, modified_by, owning_person, owning_group) values (vAdminGroupId, 'sc.languages_ex', 4, vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.group_row_access(group_id, table_name, row, created_by, modified_by, owning_person, owning_group) values (vAdminGroupId, 'sc.languages_ex', 5, vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into public.group_row_access(group_id, table_name, row, created_by, modified_by, owning_person, owning_group) values (vAdminGroupId, 'sc.languages_ex', 6, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- group memberships
    insert into public.group_memberships(group_id, person, created_by, modified_by, owning_person, owning_group) values (1, vPersonId, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    error_type := 'NoError';
  end if;

END; $$;