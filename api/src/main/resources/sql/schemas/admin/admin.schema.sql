create schema if not exists admin;
create schema if not exists common;

set schema 'common';

CREATE EXTENSION if not exists hstore;
create extension if not exists postgis with schema sc;


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
  'common.directories',
  'common.discussion_channels',
  'common.education_by_person',
  'common.education_entries',
  'common.file_versions',
  'common.files',
  'common.locations',
  'common.organizations',
  'common.people_to_org_relationships',
  'common.posts',
  'common.scripture_references',
  'common.site_text',
  'common.stage_graph',
  'common.stage_notifications',
  'common.stage_role_column_grants'
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
  'sil.table_of_languages',

  'sc.budget_records',
  'sc.budgets',
  'sc.ceremonies'
  'sc.change_to_plans',
  'sc.field_regions',
  'sc.field_zones',
  'sc.funding_accounts',
  'sc.internship_engagements',
  'sc.known_languages_by_person',
  'sc.language_engagements',
  'sc.languages',
  'sc.locations',
  'sc.organization_locations',
  'sc.organizations',
  'sc.partners',
  'sc.partnerships',
  'sc.people',
  'sc.person_unavailabilities',
  'sc.pinned_projects',
  'sc.posts',
  'sc.product_scripture_references',
  'sc.products',
  'sc.project_locations',
  'sc.project_members',
  'sc.projects',

  'sc.language_goal_definitions',
  'sc.language_locations',
  'sc.language_goals',
  'sc.periodic_reports'
);

-- VERSION CONTROL ---------------------------------------------------

create type admin.db_vc_status as enum (
  'In Progress',
  'Completed',
  'Abandoned'
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
  time_zone varchar(32),
  title varchar(255),
	status varchar(32),
  
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

  name varchar(64) not null unique,
  parent_group int references admin.groups(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int references admin.groups(id) -- not null doesn't work here, on startup
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
  owning_group int not null references admin.groups(id)
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
  owning_group int not null references admin.groups(id)
);

-- PEER to PEER -------------------------------------------------------------

create table admin.peers (
  id serial primary key,

  person int references admin.people(id),
  url varchar(128) not null unique,
  peer_approved bool not null default false,
  url_confirmed bool not null default false,
  source_token varchar(64),
  target_token varchar(64),
  session_token varchar(64),
  
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

	unique (owning_group, name)
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

-- AUTHENTICATION ------------------------------------------------------------

create table if not exists admin.tokens (
	id serial primary key,
	token varchar(64) unique,
	person int references admin.people(id),
	created_at timestamp not null default CURRENT_TIMESTAMP
);

-- email tokens

create table admin.email_tokens (
	id serial primary key,
	token varchar(512),
	email varchar(255),
	unique(token),
	created_at timestamp not null default CURRENT_TIMESTAMP
-- 	foreign key (email) references users(email)
);

-- USERS ---------------------------------------------------------------------

create table admin.users(
  id serial primary key,

  person int not null references admin.people(id),
  email varchar(255) unique not null,
  password varchar(255),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);
