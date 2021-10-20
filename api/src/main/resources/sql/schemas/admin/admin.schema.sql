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
  'admin.email_tokens',
  'admin.role_column_grants',
  'admin.role_memberships',
  'admin.role_table_permissions',
  'admin.roles',
  'admin.groups',
  'admin.group_memberships',
  'admin.group_row_access',
  'admin.people',
  'admin.tokens',
  'admin.users',

  'common.chats',
  'common.education_by_person',
  'common.education_entries',
  'common.locations',
  'common.organizations',
  'common.people_to_org_relationships',
  'common.people_to_org_relationship_type',
  'common.posts',
  'common.projects',
  'common.scripture_references',
  'common.stages',
  'common.stage_notifications',
  'common.stage_options',
  'common.tickets',
  'common.ticket_assignments',
  'common.ticket_feedback',
  'common.ticket_feedback_options',
  'common.users',
  'common.work_estimates',
  'common.work_orders',
  'common.work_order_templates',
  'common.work_records',
  'common.workflows',

  'sil.language_codes',
  'sil.country_codes',
  'sil.language_index',
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

  chat int,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int, -- not null doesn't work here, on startup
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int, -- not null doesn't work here, on startup
  owning_person int, -- not null doesn't work here, on startup
  owning_group int, -- not null doesn't work here, on startup
  peer int
);

alter table admin.people add constraint admin_people_created_by_fk foreign key (created_by) references admin.people(id);
alter table admin.people add constraint admin_people_modified_by_fk foreign key (modified_by) references admin.people(id);
alter table admin.people add constraint admin_people_owning_person_fk foreign key (owning_person) references admin.people(id);


-- GROUPS --------------------------------------------------------------------

create table admin.groups(
  id serial primary key,

  name varchar(64) not null unique,
  parent_group int references admin.groups(id),

  chat int,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int references admin.groups(id), -- not null doesn't work here, on startup
  peer int
);

alter table admin.people add constraint admin_people_owning_group_fk foreign key (owning_group) references admin.groups(id);

create table admin.group_row_access(
  id serial primary key,

  group_id int not null references admin.groups(id),
  table_name admin.table_name not null,
  row int not null,

  chat int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int
);

create table admin.group_memberships(
  id serial primary key,

  group_id int not null references admin.groups(id),
  person int not null references admin.people(id),

  chat int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int
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

  chat int,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int
);

alter table admin.people add constraint admin_people_peer_fk foreign key (peer) references admin.peers(id);
alter table admin.groups add constraint admin_groups_peer_fk foreign key (peer) references admin.peers(id);
alter table admin.group_row_access add constraint admin_group_row_access_peer_fk foreign key (peer) references admin.peers(id);
alter table admin.group_memberships add constraint admin_group_memberships_peer_fk foreign key (peer) references admin.peers(id);
alter table admin.peers add constraint admin_peers_peer_fk foreign key (peer) references admin.peers(id);

-- ROLES --------------------------------------------------------------------

create table admin.roles (
	id serial primary key,

	name varchar(255) not null,

  chat int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id),

	unique (owning_group, name)
);

create table admin.role_column_grants(
	id serial primary key,

	role int not null references admin.roles(id),
	table_name admin.table_name not null,
	column_name varchar(64) not null,
	access_level admin.access_level not null,

  chat int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id),

	unique (role, table_name, column_name)
);

create table admin.role_table_permissions(
  id serial primary key,

  role int not null references admin.roles(id),
  table_name admin.table_name not null,
  table_permission admin.table_permission not null,

  chat int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id),

  unique (role, table_name, table_permission)
);

create table admin.role_memberships (
  id serial primary key,

	role int not null references admin.roles(id),
	person int not null references admin.people(id),

  chat int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id),

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
  password varchar(255) not null,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);
