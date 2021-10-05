create schema if not exists public;

set schema 'public';
CREATE EXTENSION hstore;

create type public.sensitivity as enum (
  'Low',
  'Medium',
  'High'
);

create type public.access_level as enum (
  'Read',
  'Write'
);

create type public.table_name as enum (
  'public.education_by_person',
  'public.education_entries',
  'public.global_role_column_grants',
  'public.global_role_memberships',
  'public.global_role_table_permissions',
  'public.global_roles',
  'public.locations',
  'public.organization_grants',
  'public.organization_memberships',
  'public.organizations',
  'public.people',
  'public.people_to_org_relationship_type',
  'public.people_to_org_relationships',
  'public.project_member_roles',
  'public.project_memberships',
  'public.project_role_column_grants',
  'public.project_roles',
  'public.projects',
  'public.users',

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

create type public.table_permission as enum (
  'Create',
  'Delete'
);

-- PEOPLE ------------------------------------------------------------

create table public.people (
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
  sensitivity_clearance sensitivity default 'Low',
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

create table public.groups(
  id serial primary key,
  
  name varchar(64) not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int,

  unique (name),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (owning_person) references public.people(id),
  foreign key (owning_group) references public.groups(id)
);

create table public.group_row_access(
  id serial primary key,

  group_id int not null,
  table_name table_name not null,
  row int not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,
  
  foreign key (group_id) references public.groups(id),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (owning_person) references public.people(id),
  foreign key (owning_group) references public.groups(id)
);

create table public.group_memberships(
  id serial primary key,
  
  group_id int not null,
  person int not null,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,
  
  foreign key (group_id) references public.groups(id),
  foreign key (person) references public.people(id),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (owning_person) references public.people(id),
  foreign key (owning_group) references public.groups(id)
);

-- ROLES --------------------------------------------------------------------

create table public.global_roles (
	id serial primary key,
	
	name varchar(255) not null,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
	owning_person int not null,
	owning_group int not null,

	unique (owning_group, name),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (owning_person) references public.people(id),
  foreign key (owning_group) references public.groups(id)
);

create table public.global_role_column_grants(
	id serial primary key,
	
	global_role int not null,
	table_name public.table_name not null,
	column_name varchar(64) not null,
	access_level access_level not null,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

	unique (global_role, table_name, column_name),
	foreign key (global_role) references public.global_roles(id),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (owning_person) references public.people(id),
  foreign key (owning_group) references public.groups(id)
);

create table public.global_role_table_permissions(
  id serial primary key,

  global_role int not null,
  table_name table_name not null,
  table_permission table_permission not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

  unique (global_role, table_name, table_permission),
  foreign key (global_role) references public.global_roles(id),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (owning_person) references public.people(id),
  foreign key (owning_group) references public.groups(id)
);

create table public.global_role_memberships (
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
	foreign key (global_role) references public.global_roles(id),
	foreign key (person) references public.people(id),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (owning_person) references public.people(id),
  foreign key (owning_group) references public.groups(id)
);

-- AUTHENTICATION ------------------------------------------------------------

create table if not exists public.tokens (
	id serial primary key,
	token varchar(64),
	person int,
	unique(token),
	created_at timestamp not null default CURRENT_TIMESTAMP
	-- foreign key (person) references people(id)
);

create table if not exists public.email_tokens (
	id serial primary key,
	token varchar(512),
	email varchar(255),
	unique(token),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	foreign key (email) references users(email)
);