CREATE OR REPLACE PROCEDURE roles_migration(
  -- add params if needed
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vProjectManagerRoleId int;
  vProjectManagersGroupId int;
BEGIN
  -- create roles and groups to replicate the cord field permissions
  -- use bootstrap.sql for examples

  -- roles
  insert into admin.roles(name, created_by, modified_by, owning_person, owning_group) values ('Project Manager', 1, 1, 1, 1) returning id into vProjectManagerRoleId;

  -- groups
  insert into admin.groups(name, created_by, modified_by, owning_person, owning_group) values ('Project Managers', 1, 1, 1, 1) returning id into vProjectManagersGroupId;

  -- table grants
  insert into admin.role_table_permissions(role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values (vProjectManagerRoleId, 'Create', 'admin.people', 1, 1, 1, 1);

  -- column grants
  insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group) values ('Write', 'id', vProjectManagerRoleId, 'admin.people', 1, 1, 1, 1);

END; $$;