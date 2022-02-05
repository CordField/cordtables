CREATE EXTENSION if not exists hstore;
create extension if not exists postgis;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE OR REPLACE FUNCTION nanoid(size int DEFAULT 11)
RETURNS text AS $$
DECLARE
  id text := '';
  i int := 0;
  urlAlphabet char(64) := 'ModuleSymbhasOwnPr-0123456789ABCDEFGHNRVfgctiUvz_KqYTJkLxpZXIjQW';
  bytes bytea := gen_random_bytes(size);
  byte int;
  pos int;
BEGIN
  WHILE i < size LOOP
    byte := get_byte(bytes, i);
    pos := (byte & 63) + 1; -- + 1 because substr starts at 1 for some reason
    id := id || substr(urlAlphabet, pos, 1);
    i = i + 1;
  END LOOP;
  RETURN id;
END
$$ LANGUAGE PLPGSQL STABLE;

create type history_event_type as enum (
  'INSERT',
  'UPDATE',
  'DELETE'
);

create table common_sensitivity_enum (
  value varchar(32) primary key
);

insert into common_sensitivity_enum(value) values('Low'), ('Medium'), ('High');

create table admin_access_level_enum(
  value varchar(32) primary key
);

insert into admin_access_level_enum(value) values('Read'), ('Write');

create table admin_table_name_enum (
  value varchar(64) primary key
);

-- VERSION CONTROL ---------------------------------------------------

create type db_vc_status as enum (
  'In Progress',
  'Completed',
  'Abandoned'
);

create table database_version_control_x (
  id varchar(32) primary key default nanoid(),
  version int not null,
  status db_vc_status default 'In Progress',
  started timestamp not null default CURRENT_TIMESTAMP,
  completed timestamp
);

-- PEOPLE ------------------------------------------------------------

create table admin_people (
  about text,
  picture_files_id varchar(32), -- todo references common_files
  private_first_name varchar(32),
  private_last_name varchar(32),
  public_first_name varchar(32),
  public_last_name varchar(32),
  primary_location_locations_id varchar(32), -- todo references common_locations(id),
  sensitivity_clearance varchar(32) references common_sensitivity_enum(value) default 'Low',
  timezone varchar(64),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32), -- not null doesn't work here, on startup
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32), -- not null doesn't work here, on startup
  owning_person_people_id varchar(32), -- not null doesn't work here, on startup
  owning_group_groups_id varchar(32) -- not null doesn't work here, on startup
);

alter table admin_people add constraint admin_people_created_by_people_id_fk foreign key (created_by_people_id) references admin_people(id);
alter table admin_people add constraint admin_people_modified_by_people_id_fk foreign key (modified_by_people_id) references admin_people(id);
alter table admin_people add constraint admin_people_owning_person_people_id_fk foreign key (owning_person_people_id) references admin_people(id);

-- GROUPS --------------------------------------------------------------------

create table groups(
  name varchar(64) not null,
  parent_group_row_access_groups_id varchar(32) references groups(id),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id),

  unique (name, owning_group_groups_id)
);

alter table admin_people add constraint admin_people_owning_group_groups_id_fk foreign key (owning_group_groups_id) references groups(id);

create table group_row_access(
  groups_id varchar(32) not null references groups(id),
  table_name varchar(64) not null references admin_table_name_enum(value),
  row_id varchar(32) not null,

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id),

  unique (groups_id, table_name, row_id)
);

create table group_memberships(
  group_id varchar(32) not null references groups(id),
  person varchar(32) not null references admin_people(id),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id),

  unique (group_id, person)
);

-- PEER to PEER -------------------------------------------------------------

create table peers (
  person varchar(32) unique not null references admin_people(id),
  url varchar(128) unique not null,
  peer_approved bool not null default false,
  url_confirmed bool not null default false,
  source_token varchar(64) unique,
  target_token varchar(64) unique,
  session_token varchar(64) unique,

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id)
);

-- ROLES --------------------------------------------------------------------

create table roles (
	name varchar(255) not null,

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id),

	unique (name, owning_group_groups_id)
);

create table role_column_grants(
	role varchar(32) not null references roles(id),
	table_name varchar(64) not null references admin_table_name_enum(value),
	column_name varchar(64) not null,
	access_level varchar(32) not null references admin_access_level_enum(value),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id),

	unique (role, table_name, column_name)
);

create type table_permission_grant_type as enum (
  'Create',
  'Delete'
);

create table role_table_permissions(
  role varchar(32) not null references roles(id),
  table_name varchar(64) not null references admin_table_name_enum(value),
  table_permission table_permission_grant_type not null,

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id),

  unique (role, table_name, table_permission)
);

create table role_memberships (
	role varchar(32) not null references roles(id),
	person varchar(32) unique not null references admin_people(id),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id),

	unique(role, person)
);

-- USERS ---------------------------------------------------------------------

create table user_email_accounts(
  email varchar(255) unique,
  password varchar(255),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id)
);

create table user_phone_accounts(
  phone varchar(64) unique,
  password varchar(255),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_people_id varchar(32) references admin_people(id),
  owning_person_people_id varchar(32) references admin_people(id),
  owning_group_groups_id varchar(32) references groups(id)
);

-- AUTHENTICATION ------------------------------------------------------------

create table if not exists tokens (
	id varchar(32) primary key default nanoid(),
	token varchar(64) unique not null,
	person varchar(32) references admin_people(id),
	created_at timestamp not null default CURRENT_TIMESTAMP
);

-- email tokens

create table email_tokens (
	id varchar(32) primary key default nanoid(),
	token varchar(512) unique not null,
	user_id varchar(32) not null references user_email_accounts(id),
	created_at timestamp not null default CURRENT_TIMESTAMP
);

insert into admin_table_name_enum(value) values
  ('admin_database_version_control'),
  ('admin_email_tokens'),
  ('admin_group_memberships'),
  ('admin_group_row_access'),
  ('admin_groups'),
  ('admin_peers'),
  ('admin_people'),
  ('admin_role_column_grants'),
  ('admin_role_memberships'),
  ('admin_role_table_permissions'),
  ('admin_roles'),
  ('admin_tokens'),
  ('admin_users');

insert into admin_table_name_enum(value) values
  ('common_blogs'),
  ('common_blog_posts'),
  ('common_cell_channels'),
  ('common_coalition_memberships'),
  ('common_coalitions'),
  ('common_directories'),
  ('common_discussion_channels'),
  ('common_education_by_person'),
  ('common_education_entries'),
  ('common_file_versions'),
  ('common_files'),
  ('common_languages'),
  ('common_locations'),
  ('common_notes'),
  ('common_organizations'),
  ('common_org_chart_positions'),
  ('common_org_chart_position_graph'),
  ('common_people_graph'),
  ('common_people_to_org_relationships'),
  ('common_posts'),
  ('common_up.prayer_requests'),
  ('common_up.prayer_notifications'),
  ('common_scripture_references'),
  ('common_site_text_strings'),
  ('common_site_text_translations'),
  ('common_stage_graph'),
  ('common_stage_notifications'),
  ('common_stage_role_column_grants'),
  ('common_stages'),
  ('common_threads'),
  ('common_ticket_assignments'),
  ('common_ticket_feedback'),
  ('common_ticket_graph'),
  ('common_tickets'),
  ('common_work_estimates'),
  ('common_work_records'),
  ('common_workflows');

insert into admin_table_name_enum(value) values
  ('sil_country_codes'),
  ('sil_language_codes'),
  ('sil_language_index'),
  ('sil_iso_639_3'),
  ('sil_iso_639_3_names'),
  ('sil_iso_639_3_macrolanguages'),
  ('sil_iso_639_3_retirements'),
  ('sil_table_of_countries'),
  ('sil_table_of_languages'),
  ('sil_table_of_languages_in_country');

insert into admin_table_name_enum(value) values
  ('sc_budget_records'),
  ('sc_budget_records_partnerships'),
  ('sc_budgets'),
  ('sc_ceremonies'),
  ('sc_change_to_plans'),
  ('sc_ethno_arts'),
  ('sc_ethnologue'),
  ('sc_field_regions'),
  ('sc_field_zones'),
  ('sc_file_versions'),
  ('sc_films'),
  ('sc_funding_accounts'),
  ('sc_global_partner_assessments'),
  ('sc_global_partner_engagements'),
  ('sc_global_partner_engagement_people'),
  ('sc_global_partner_performance'),
  ('sc_global_partner_transitions'),
  ('sc_internship_engagements'),
  ('sc_known_languages_by_person'),
  ('sc_language_engagements'),
  ('sc_language_goal_definitions'), -- not finishe),
  ('sc_language_goals'), -- not finishe),
  ('sc_language_locations'), -- not finishe),
  ('sc_languages'),
  ('sc_locations'),
  ('sc_organization_locations'),
  ('sc_organizations'),
  ('sc_partners'),
  ('sc_partnerships'),
  ('sc_people'),
  ('sc_periodic_reports'),
  ('sc_periodic_reports_directory'),
  ('sc_person_unavailabilities'),
  ('sc_pinned_projects'),
  ('sc_posts'),
  ('sc_posts_directory'),
  ('sc_product_scripture_references'),
  ('sc_products'),
  ('sc_project_locations'),
  ('sc_project_members'),
  ('sc_projects'),
  ('sc_stories');