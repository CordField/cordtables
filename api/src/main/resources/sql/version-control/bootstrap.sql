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

  vPublicPersonId int;
  vPublicGroupId int;
  vPublicRoleId int;
BEGIN
  select count(id)
  from admin.people
  into vPeopleCount;

  if vPeopleCount = 0 then
    -- people
    insert into admin.people(sensitivity_clearance)
    values ('High')
    returning id
    into vPersonId;

    insert into admin.people(sensitivity_clearance)
    values ('Low')
    returning id
    into vPublicPersonId;

    -- create token for the public 'person'
    insert into admin.tokens(token, person) values ('public', vPublicPersonId);

    -- groups
    insert into admin.groups(name, created_by, modified_by, owning_person)
    values ('Administrators', vPersonId, vPersonId, vPersonId)
    returning id
    into vAdminGroupId;

    insert into admin.groups(name, created_by, modified_by, owning_person)
    values ('Public', vPersonId, vPersonId, vPersonId)
    returning id
    into vPublicGroupId;

    -- organization
    insert into common.organizations(name, sensitivity, created_by, modified_by, owning_person, owning_group)
    values ('Seed Company', 'Low', vPersonId, vPersonId, vPersonId, vAdminGroupId)
    returning id
    into vOrgId;

    -- users
    insert into common.users(person, email, password, created_by, modified_by, owning_person, owning_group)
    values (vPersonId, p_email, p_password, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- global roles
    insert into admin.global_roles(name, created_by, modified_by, owning_person, owning_group)
    values ('Administrator', vPersonId, vPersonId, vPersonId, vAdminGroupId)
    returning id
    into vAdminRoleId;

    insert into admin.global_roles(name, created_by, modified_by, owning_person, owning_group)
    values ('Public', vPersonId, vPersonId, vPersonId, vAdminGroupId)
    returning id
    into vPublicRoleId;

    -- global role memberships
    insert into admin.global_role_memberships(global_role, person, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, vPersonId, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- global role table grants
    insert into admin.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Create', 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Delete', 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Create', 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Delete', 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Create', 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Delete', 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Create', 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_table_permissions(global_role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vAdminRoleId, 'Delete', 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on people
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'neo4j', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'about', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_at', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_by', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_at', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_by', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'phone', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'picture', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'primary_org', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'private_first_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'private_last_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'public_first_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'public_last_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'primary_location', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'private_full_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'public_full_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'sensitivity_clearance', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'time_zone', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'title', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'status', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on global_role_memberships
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vAdminRoleId, 'admin.global_role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'global_role', vAdminRoleId, 'admin.global_role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'table_name', vAdminRoleId, 'admin.global_role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'column_name', vAdminRoleId, 'admin.global_role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'access_level', vAdminRoleId, 'admin.global_role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_at', vAdminRoleId, 'admin.global_role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_by', vAdminRoleId, 'admin.global_role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'owning_person', vAdminRoleId, 'admin.global_role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'owning_group', vAdminRoleId, 'admin.global_role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on users
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vAdminRoleId, 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'person', vAdminRoleId, 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'owning_org', vAdminRoleId, 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'email', vAdminRoleId, 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'password', vAdminRoleId, 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_at', vAdminRoleId, 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_by', vAdminRoleId, 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_at', vAdminRoleId, 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_by', vAdminRoleId, 'common.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on organizations
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'neo4j', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_at', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_by', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_at', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_by', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'name', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'sensitivity', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'primary_location', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'name', vPublicRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on sc.languages_ex
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'iso', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'prioritization', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'progress_bible', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'location_long', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'island', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'province', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'first_language_population', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'population_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'egids_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'egids_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'least_reached_progress_jps_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'least_reached_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'partner_interest_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'partner_interest_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'partner_interest_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'partner_interest_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multiple_languages_leverage_linguistic_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multiple_languages_leverage_linguistic_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multiple_languages_leverage_linguistic_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multiple_languages_leverage_linguistic_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multiple_languages_leverage_joint_training_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multiple_languages_leverage_joint_training_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multiple_languages_leverage_joint_training_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'multiple_languages_leverage_joint_training_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_comm_int_in_language_development_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_comm_int_in_language_development_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_comm_int_in_language_development_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_comm_int_in_language_development_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_comm_int_in_scripture_translation_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_comm_int_in_scripture_translation_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_comm_int_in_scripture_translation_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'lang_comm_int_in_scripture_translation_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'access_to_scripture_in_lwc_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'access_to_scripture_in_lwc_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'access_to_scripture_in_lwc_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'access_to_scripture_in_lwc_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'begin_work_geo_challenges_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'begin_work_geo_challenges_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'begin_work_geo_challenges_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'begin_work_geo_challenges_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'begin_work_rel_pol_obstacles_level', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'begin_work_rel_pol_obstacles_value', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'begin_work_rel_pol_obstacles_description', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'begin_work_rel_pol_obstacles_source', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'suggested_strategies', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'comments', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_at', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'created_by', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_at', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'modified_by', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'owning_person', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);
    insert into admin.global_role_column_grants(access_level, column_name, global_role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'owning_group', vAdminRoleId, 'sc.languages_ex', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- group memberships
    insert into admin.group_memberships(group_id, person, created_by, modified_by, owning_person, owning_group) values (1, vPersonId, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    error_type := 'NoError';
  end if;

END; $$;