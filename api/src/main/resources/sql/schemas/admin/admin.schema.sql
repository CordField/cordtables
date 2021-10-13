create schema admin;
create schema if not exists common;

set schema 'common';

CREATE EXTENSION if not exists hstore;

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
  'admin.global_role_column_grants',
  'admin.global_role_memberships',
  'admin.global_role_table_permissions',
  'admin.global_roles',
  'admin.groups',
  'admin.group_memberships',
  'admin.group_row_access',
  'admin.people',

  'common.people_to_org_relationship_type',
  'common.people_to_org_relationships',
  'common.project_member_roles',
  'common.project_memberships',
  'common.project_role_column_grants',
  'common.project_roles',
  'common.organization_grants',
  'common.organization_memberships',
  'common.organizations',
  'common.education_by_person',
  'common.education_entries',
  'common.locations',
  'common.projects',
  'common.users',

  'sil.language_codes',
  'sil.country_codes',
  'sil.table_of_languages',

  'sc.funding_account',
  'sc.field_zone',
  'sc.field_regions',
  'sc.locations',
  'sc.organizations',
  'sc.organization_locations',
  'sc.partners',
  'sc.language_goal_definitions',
  'sc.languages',
  'sc.languages_ex',
  'sc.language_locations',
  'sc.language_goals',
  'sc.known_languages_by_person',
  'sc.people',
  'sc.person_unavailabilities',
  'sc.directories',
  'sc.files',
  'sc.file_versions',
  'sc.projects',
  'sc.partnerships',
  'sc.change_to_plans',
  'sc.periodic_reports',
  'sc.posts',
  'sc.budgets',
  'sc.budget_records',
  'sc.project_locations',
  'sc.project_members',
  'sc.project_member_roles',
  'sc.language_engagements',
  'sc.products',
  'sc.product_scripture_references',
  'sc.internship_engagements',
  'sc.ceremonies'
);

create type admin.table_permission as enum (
  'Create',
  'Delete'
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
  created_by int,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int,
  owning_person int,
  owning_group int
);

-- GROUPS --------------------------------------------------------------------

create table admin.groups(
  id serial primary key,

  name varchar(64) not null,
  parent_group int,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int,

  unique (name),
  foreign key (parent_group) references admin.groups(id),
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (owning_person) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);

create table admin.group_row_access(
  id serial primary key,

  group_id int not null,
  table_name admin.table_name not null,
  row int not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

  foreign key (group_id) references admin.groups(id),
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (owning_person) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);

create table admin.group_memberships(
  id serial primary key,

  group_id int not null,
  person int not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

  foreign key (group_id) references admin.groups(id),
  foreign key (person) references admin.people(id),
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (owning_person) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);

-- ROLES --------------------------------------------------------------------

create table admin.global_roles (
	id serial primary key,

	name varchar(255) not null,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
	owning_person int not null,
	owning_group int not null,

	unique (owning_group, name),
	foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (owning_person) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);

create table admin.global_role_column_grants(
	id serial primary key,

	global_role int not null,
	table_name admin.table_name not null,
	column_name varchar(64) not null,
	access_level admin.access_level not null,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

	unique (global_role, table_name, column_name),
	foreign key (global_role) references admin.global_roles(id),
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (owning_person) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);

create table admin.global_role_table_permissions(
  id serial primary key,

  global_role int not null,
  table_name admin.table_name not null,
  table_permission admin.table_permission not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

  unique (global_role, table_name, table_permission),
  foreign key (global_role) references admin.global_roles(id),
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (owning_person) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);

create table admin.global_role_memberships (
  id serial primary key,

	global_role int not null,
	person int not null,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

	unique(global_role,person),
	foreign key (global_role) references admin.global_roles(id),
	foreign key (person) references admin.people(id),
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (owning_person) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);

-- AUTHENTICATION ------------------------------------------------------------

create table if not exists admin.tokens (
	id serial primary key,
	token varchar(64),
	person int,
	unique(token),
	created_at timestamp not null default CURRENT_TIMESTAMP
	-- foreign key (person) references people(id)
);

-- ORGANIZATIONS ------------------------------------------------------------

--create table if not exists common.organizations (
--	id serial primary key,
--
--	name varchar(255) unique not null,
--	neo4j_id varchar(32),
--	sensitivity common.sensitivity default 'High',
--	primary_location int,
--
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null,
--	modified_at timestamp not null default CURRENT_TIMESTAMP,
--  modified_by int not null,
--  owning_person int not null,
--  owning_group int not null,
--
--	foreign key (primary_location) references locations(id),
--	foreign key (created_by) references admin.people(id),
--  foreign key (modified_by) references admin.people(id),
--  foreign key (owning_group) references admin.groups(id)
--);