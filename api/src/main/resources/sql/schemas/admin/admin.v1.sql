create schema if not exists admin;
create schema if not exists common;

set schema 'common';

CREATE EXTENSION if not exists hstore;
create extension if not exists postgis;


create type common.sensitivity as enum (
  'Low',
  'Medium',
  'High'
);

create type admin.access_level as enum (
  'Read',
  'Write'
);

create type admin.table_name as enum (
  'admin.database_version_control',
  'admin.email_tokens',
  'admin.group_memberships',
  'admin.group_row_access',
  'admin.groups',
  'admin.peers',
  'admin.people',
  'admin.role_column_grants',
  'admin.role_memberships',
  'admin.role_table_permissions',
  'admin.roles',
  'admin.tokens',
  'admin.users',

  'common.blogs',
  'common.blog_posts',
  'common.cell_channels',
  'common.coalition_memberships',
  'common.coalitions',
  'common.directories',
  'common.discussion_channels',
  'common.education_by_person',
  'common.education_entries',
  'common.file_versions',
  'common.files',
  'common.languages',
  'common.locations',
  'common.notes',
  'common.organizations',
  'common.org_chart_positions',
  'common.org_chart_position_graph',
  'common.people_graph',
  'common.people_to_org_relationships',
  'common.posts',
  'up.prayer_requests',
  'up.prayer_notifications',
  'common.scripture_references',
  'common.site_text_strings',
  'common.site_text_translations',
  'common.stage_graph',
  'common.stage_notifications',
  'common.stage_role_column_grants',
  'common.stages',
  'common.threads',
  'common.ticket_assignments',
  'common.ticket_feedback',
  'common.ticket_graph',
  'common.tickets',
  'common.work_estimates',
  'common.work_records',
  'common.workflows',

  'sil.country_codes',
  'sil.language_codes',
  'sil.language_index',
  'sil.iso_639_3',
  'sil.iso_639_3_names',
  'sil.iso_639_3_macrolanguages',
  'sil.iso_639_3_retirements',
  'sil.table_of_countries',
  'sil.table_of_languages',
  'sil.table_of_languages_in_country',

  'sc.budget_records',
  'sc.budget_records_partnerships',
  'sc.budgets',
  'sc.ceremonies',
  'sc.change_to_plans',
  'sc.ethnologue',
  'sc.field_regions',
  'sc.field_zones',
  'sc.file_versions',
  'sc.funding_accounts',
  'sc.global_partner_assessments',
  'sc.global_partner_engagements',
  'sc.global_partner_engagement_people',
  'sc.global_partner_performance',
  'sc.global_partner_transitions',
  'sc.internship_engagements',
  'sc.known_languages_by_person',
  'sc.language_engagements',
  'sc.language_goal_definitions', -- not finished
  'sc.language_goals', -- not finished
  'sc.language_locations', -- not finished
  'sc.languages',
  'sc.locations',
  'sc.organization_locations',
  'sc.organizations',
  'sc.partners',
  'sc.partnerships',
  'sc.people',
  'sc.periodic_reports',
  'sc.periodic_reports_directory',
  'sc.person_unavailabilities',
  'sc.pinned_projects',
  'sc.posts',
  'sc.posts_directory',
  'sc.producible',
  'sc.producible_scripture_references',
  'sc.product_scripture_references',
  'sc.products',
  'sc.project_locations',
  'sc.project_members',
  'sc.projects'
);

-- VERSION CONTROL ---------------------------------------------------

create type admin.db_vc_status as enum (
  'In Progress',
  'Completed',
  'Abandoned'
);

create type admin.row_status as enum (
  'Active',
  'Inactive'
);

create table admin.database_version_control (
  id serial primary key,
  version int not null,
  status admin.db_vc_status default 'In Progress',
  started timestamp not null default CURRENT_TIMESTAMP,
  completed timestamp
);

-- PEOPLE ------------------------------------------------------------

create table admin.people (
  id serial primary key,
  neo4j_id varchar(32),
  about text,
  phone varchar(32),
  picture varchar(255),
  private_first_name varchar(32),
  private_last_name varchar(32),
  public_first_name varchar(32),
  public_last_name varchar(32),
  primary_location int,
  private_full_name varchar(64),
  public_full_name varchar(64),
  sensitivity_clearance common.sensitivity default 'Low',
  timezone varchar(32),
  title varchar(255),
  status admin.row_status,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int, -- not null doesn't work here, on startup
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int, -- not null doesn't work here, on startup
  owning_person int, -- not null doesn't work here, on startup
  owning_group int -- not null doesn't work here, on startup
);

alter table admin.people add constraint admin_people_created_by_fk foreign key (created_by) references admin.people(id);
alter table admin.people add constraint admin_people_modified_by_fk foreign key (modified_by) references admin.people(id);
alter table admin.people add constraint admin_people_owning_person_fk foreign key (owning_person) references admin.people(id);


-- GROUPS --------------------------------------------------------------------

create table admin.groups(
  id serial primary key,

  name varchar(64) not null,
  parent_group int references admin.groups(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int references admin.groups(id), -- not null doesn't work here, on startup

  unique (name, owning_group)
);

alter table admin.people add constraint admin_people_owning_group_fk foreign key (owning_group) references admin.groups(id);

create table admin.group_row_access(
  id serial primary key,

  group_id int not null references admin.groups(id),
  table_name admin.table_name not null,
  row int not null,
  
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (group_id, table_name, row)
);

create table admin.group_memberships(
  id serial primary key,

  group_id int not null references admin.groups(id),
  person int not null references admin.people(id),
  
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (group_id, person)
);

-- PEER to PEER -------------------------------------------------------------

create table admin.peers (
  id serial primary key,

  person int unique not null references admin.people(id),
  url varchar(128) unique not null,
  peer_approved bool not null default false,
  url_confirmed bool not null default false,
  source_token varchar(64) unique,
  target_token varchar(64) unique,
  session_token varchar(64) unique,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- ROLES --------------------------------------------------------------------

create table admin.roles (
	id serial primary key,

	name varchar(255) not null,
  
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (name, owning_group)
);

create table admin.role_column_grants(
	id serial primary key,

	role int not null references admin.roles(id),
	table_name admin.table_name not null,
	column_name varchar(64) not null,
	access_level admin.access_level not null,
  
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (role, table_name, column_name)
);

create type admin.table_permission_grant_type as enum (
  'Create',
  'Delete'
);

create table admin.role_table_permissions(
  id serial primary key,

  role int not null references admin.roles(id),
  table_name admin.table_name not null,
  table_permission admin.table_permission_grant_type not null,
  
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (role, table_name, table_permission)
);

create table admin.role_memberships (
  id serial primary key,

	role int not null references admin.roles(id),
	person int not null references admin.people(id),
  
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique(role, person)
);

-- USERS ---------------------------------------------------------------------

create table admin.users(
  id serial primary key,

  person int references admin.people(id) unique, -- not null added in v2
  email varchar(255) unique,
  password varchar(255),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- AUTHENTICATION ------------------------------------------------------------

create table if not exists admin.tokens (
	id serial primary key,
	token varchar(64) unique not null,
	person int references admin.people(id),
	created_at timestamp not null default CURRENT_TIMESTAMP
);

-- email tokens

create table admin.email_tokens (
	id serial primary key,
	token varchar(512) unique not null,
	user_id int not null references admin.users(id),
	created_at timestamp not null default CURRENT_TIMESTAMP
);
