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
  vNonAdminPersonId int;
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
    insert into admin.users(person, email, password, created_by, modified_by, owning_person, owning_group)
    values (vPersonId, p_email, p_password, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- global roles
    insert into admin.roles(name, created_by, modified_by, owning_person, owning_group)
    values ('Administrator', vPersonId, vPersonId, vPersonId, vAdminGroupId)
    returning id
    into vAdminRoleId;

    insert into admin.roles(name, created_by, modified_by, owning_person, owning_group)
    values ('Public', vPersonId, vPersonId, vPersonId, vAdminGroupId)
    returning id
    into vPublicRoleId;

    -- global role memberships
    insert into admin.role_memberships(role, person, created_by, modified_by, owning_person, owning_group) values
    (vAdminRoleId, vPersonId, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- global role table grants
    insert into admin.role_table_permissions(role, table_permission, table_name, created_by, modified_by, owning_person, owning_group)
    values (vAdminRoleId, 'Create', 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
            (vAdminRoleId, 'Delete', 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_table_permissions(role, table_permission, table_name, created_by, modified_by, owning_person, owning_group) values
    (vAdminRoleId, 'Create', 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    (vAdminRoleId, 'Delete', 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_table_permissions(role, table_permission, table_name, created_by, modified_by, owning_person, owning_group)
    values (vAdminRoleId, 'Create', 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    (vAdminRoleId, 'Delete', 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_table_permissions(role, table_permission, table_name, created_by, modified_by, owning_person, owning_group)
    values (vAdminRoleId, 'Create', 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     (vAdminRoleId, 'Delete', 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on people
    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values
    ('Write', 'id', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'neo4j', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'about', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'created_at', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'created_by', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'modified_at', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'modified_by', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'phone', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'picture', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'primary_org', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'private_first_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'private_last_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'public_first_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'public_last_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'primary_location', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'private_full_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'public_full_name', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'sensitivity_clearance', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'time_zone', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'title', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'status', vAdminRoleId, 'admin.people', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on role_memberships
    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'id', vAdminRoleId, 'admin.role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'role', vAdminRoleId, 'admin.role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'table_name', vAdminRoleId, 'admin.role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'column_name', vAdminRoleId, 'admin.role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'access_level', vAdminRoleId, 'admin.role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'created_at', vAdminRoleId, 'admin.role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'created_by', vAdminRoleId, 'admin.role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'owning_person', vAdminRoleId, 'admin.role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'owning_group', vAdminRoleId, 'admin.role_memberships', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on users
    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values
    ('Write', 'id', vAdminRoleId, 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'person', vAdminRoleId, 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'owning_org', vAdminRoleId, 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'email', vAdminRoleId, 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'password', vAdminRoleId, 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'created_at', vAdminRoleId, 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'created_by', vAdminRoleId, 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'modified_at', vAdminRoleId, 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'modified_by', vAdminRoleId, 'admin.users', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on organizations
    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'id', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'neo4j', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'created_at', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'created_by', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'modified_at', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'modified_by', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'name', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'sensitivity', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'primary_location', vAdminRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'name', vPublicRoleId, 'common.organizations', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on sc.languages
    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'id', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'prioritization', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'progress_bible', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'location_long', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'island', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'province', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'first_language_population', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'population_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'egids_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'egids_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'least_reached_progress_jps_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'least_reached_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'partner_interest_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'partner_interest_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'partner_interest_description', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'partner_interest_source', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'multiple_languages_leverage_linguistic_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'multiple_languages_leverage_linguistic_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'multiple_languages_leverage_linguistic_description', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'multiple_languages_leverage_linguistic_source', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'multiple_languages_leverage_joint_training_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'multiple_languages_leverage_joint_training_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'multiple_languages_leverage_joint_training_description', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'multiple_languages_leverage_joint_training_source', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'lang_comm_int_in_language_development_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'lang_comm_int_in_language_development_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'lang_comm_int_in_language_development_description', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'lang_comm_int_in_language_development_source', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'lang_comm_int_in_scripture_translation_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'lang_comm_int_in_scripture_translation_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'lang_comm_int_in_scripture_translation_description', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'lang_comm_int_in_scripture_translation_source', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'access_to_scripture_in_lwc_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'access_to_scripture_in_lwc_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'access_to_scripture_in_lwc_description', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'access_to_scripture_in_lwc_source', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'begin_work_geo_challenges_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'begin_work_geo_challenges_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'begin_work_geo_challenges_description', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'begin_work_geo_challenges_source', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'begin_work_rel_pol_obstacles_level', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'begin_work_rel_pol_obstacles_value', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'begin_work_rel_pol_obstacles_description', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'begin_work_rel_pol_obstacles_source', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values ('Write', 'suggested_strategies', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'comments', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
    ('Write', 'coordinates', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values
    ('Write', 'created_at', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'created_by', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'modified_at', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'modified_by', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'owning_person', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'owning_group', vAdminRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- group memberships
    insert into admin.group_memberships(group_id, person, created_by, modified_by, owning_person, owning_group)
    values (1, vPersonId, vPersonId, vPersonId, vPersonId, vAdminGroupId);

    -- grants on scripture_references
    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
    values
     ('Write', 'id', vAdminRoleId, 'common.scripture_references', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'book_start', vAdminRoleId, 'common.scripture_references', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'book_end', vAdminRoleId, 'common.scripture_references', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'chapter_start', vAdminRoleId, 'common.scripture_references', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'chapter_end', vAdminRoleId, 'common.scripture_references', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'verse_start', vAdminRoleId, 'common.scripture_references', vPersonId, vPersonId, vPersonId, vAdminGroupId),
     ('Write', 'verse_end', vAdminRoleId, 'common.scripture_references', vPersonId, vPersonId, vPersonId, vAdminGroupId);

--    grants on common.cell_channels
    insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
        values
         ('Write', 'id', vAdminRoleId, 'common.cell_channels', vPersonId, vPersonId, vPersonId, vAdminGroupId),
         ('Write', 'row', vAdminRoleId, 'common.cell_channels', vPersonId, vPersonId, vPersonId, vAdminGroupId),
         ('Write', 'table_name', vAdminRoleId, 'common.cell_channels', vPersonId, vPersonId, vPersonId, vAdminGroupId),
         ('Write', 'column_name', vAdminRoleId, 'common.cell_channels', vPersonId, vPersonId, vPersonId, vAdminGroupId);

--         grants on common.threads
         insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
                 values
                  ('Write', 'id', vAdminRoleId, 'common.threads', vPersonId, vPersonId, vPersonId, vAdminGroupId),
                  ('Write', 'channel', vAdminRoleId, 'common.threads', vPersonId, vPersonId, vPersonId, vAdminGroupId),
                  ('Write', 'content', vAdminRoleId, 'common.threads', vPersonId, vPersonId, vPersonId, vAdminGroupId);
--                  grants on common.posts
              insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
                          values
                           ('Write', 'id', vAdminRoleId, 'common.posts', vPersonId, vPersonId, vPersonId, vAdminGroupId),
                           ('Write', 'thread', vAdminRoleId, 'common.posts', vPersonId, vPersonId, vPersonId, vAdminGroupId),
                           ('Write', 'content', vAdminRoleId, 'common.posts', vPersonId, vPersonId, vPersonId, vAdminGroupId);



-- creating non-admin user
    insert into admin.users(email,password, person, created_by, modified_by, owning_person,owning_group)
    values
    ('non-admin@tsco.org','$argon2id$v=19$m=4096,t=3,p=1$wrgddJLJEp4iGr1xtX9f9A$7iicFbpW55+8wo0yoLd8kK1yToMIy6FNLXRIAtTAuLU',
     vPublicPersonId, vPersonId, vPersonId, vPersonId, vAdminGroupId)
    returning id into vNonAdminPersonId;

     insert into admin.role_memberships(role, person, created_by, modified_by, owning_person, owning_group) values
     (vPublicRoleId, vNonAdminPersonId, vPersonId, vPersonId, vPersonId,vAdminGroupId ) ;

     insert into admin.group_memberships(group_id,person,created_by,modified_by,owning_person,owning_group) values
     (vPublicGroupId, vNonAdminPersonId, vPersonId, vPersonId, vPersonId, vAdminGroupId);
 --    only giving the user create permission so we can test if they have create and don't have delete
     insert into admin.role_table_permissions(role, table_permission, table_name, created_by, modified_by, owning_person, owning_group)
     values (vPublicRoleId, 'Create', 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

      insert into admin.role_column_grants(access_level, column_name, role, table_name, created_by, modified_by, owning_person, owning_group)
       values
       ('Write', 'id', vPublicRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId),
       ('Write', 'prioritization', vPublicRoleId, 'sc.languages', vPersonId, vPersonId, vPersonId, vAdminGroupId);

      -- giving row membership to only one row
      insert into admin.group_row_access(group_id,table_name,row,created_by,modified_by,owning_person,owning_group)
      values(vPublicGroupId,'sc.languages',1,vPersonId,vPersonId,vPersonId,vAdminGroupId);

    error_type := 'NoError';
  end if;

END; $$;