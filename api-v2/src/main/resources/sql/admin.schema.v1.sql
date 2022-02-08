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
  picture_common_files_id varchar(32), -- todo references common_files
  private_first_name varchar(32),
  private_last_name varchar(32),
  public_first_name varchar(32),
  public_last_name varchar(32),
  primary_location_common_locations_id varchar(32), -- todo references common_locations(id),
  sensitivity_clearance varchar(32) references common_sensitivity_enum(value) default 'Low',
  timezone varchar(64),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32), -- not null doesn't work here, on startup
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32), -- not null doesn't work here, on startup
  owning_person_admin_people_id varchar(32), -- not null doesn't work here, on startup
  owning_group_admin_groups_id varchar(32) -- not null doesn't work here, on startup
);

alter table admin_people add constraint admin_people_created_by_admin_people_id_fk foreign key (created_by_admin_people_id) references admin_people(id);
alter table admin_people add constraint admin_people_modified_by_admin_people_id_fk foreign key (modified_by_admin_people_id) references admin_people(id);
alter table admin_people add constraint admin_people_owning_person_admin_people_id_fk foreign key (owning_person_admin_people_id) references admin_people(id);

-- GROUPS --------------------------------------------------------------------

create table admin_groups(
  name varchar(64) not null,
  parent_group_admin_groups_id varchar(32) references admin_groups(id),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id),

  unique (name, owning_group_admin_groups_id)
);

alter table admin_people add constraint admin_people_owning_group_admin_groups_id_fk foreign key (owning_group_admin_groups_id) references admin_groups(id);

create table admin_group_row_access(
  admin_groups_id varchar(32) not null references admin_groups(id),
  table_name varchar(64) not null references admin_table_name_enum(value),
  row_id varchar(32) not null,

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id),

  unique (admin_groups_id, table_name, row_id)
);

create table admin_group_memberships(
  group_admin_group_id varchar(32) not null references admin_groups(id),
  person_admin_people_id varchar(32) not null references admin_people(id),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id),

  unique (group_admin_group_id, person_admin_people_id)
);

create table admin_organization_administrators(
  group_admin_group_id varchar(32) not null references admin_groups(id),
  person_admin_people_id varchar(32) not null references admin_people(id),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id),

  unique (group_admin_group_id, person_admin_people_id)
);

-- ROLES --------------------------------------------------------------------

create table admin_roles (
	name varchar(255) not null,

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id),

	unique (name, owning_group_admin_groups_id)
);

create table admin_role_column_grants(
	role varchar(32) not null references admin_roles(id),
	table_name varchar(64) not null references admin_table_name_enum(value),
	column_name varchar(64) not null,
	access_level varchar(32) not null references admin_access_level_enum(value),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id),

	unique (role, table_name, column_name)
);

create table admin_table_permission_grant_enum (
  value varchar(32) primary key
);

insert into admin_table_permission_grant_enum(value) values ('Create'),('Delete');

create table admin_role_table_permissions(
  role_admin_role_id varchar(32) not null references admin_roles(id),
  table_name varchar(64) not null references admin_table_name_enum(value),
  table_permission varchar(32) not null references admin_table_permission_grant_enum(value),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id),

  unique (role_admin_role_id, table_name, table_permission)
);

create table admin_role_memberships (
	role_admin_role_id varchar(32) not null references admin_roles(id),
	person_admin_people_id varchar(32) unique not null references admin_people(id),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id),

	unique(role_admin_role_id, person_admin_people_id)
);

create table admin_role_all_data_column_grants(
	role_admin_role_id varchar(32) not null references admin_roles(id),
	table_name varchar(64) not null references admin_table_name_enum(value),
	column_name varchar(64) not null,
	access_level varchar(32) not null references admin_access_level_enum(value),

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id),

	unique (role_admin_role_id, table_name, column_name)
);

-- USERS ---------------------------------------------------------------------

create table admin_user_email_accounts(
  email varchar(255) unique not null,
  password varchar(255) not null,

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id)
);

create table admin_user_phone_accounts(
  phone varchar(64) unique not null,
  password varchar(255) not null,

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id)
);

-- AUTHENTICATION ------------------------------------------------------------

create table if not exists admin_tokens_x (
	id varchar(32) primary key default nanoid(),
	token varchar(64) unique not null,
	person varchar(32) references admin_people(id),
	created_at timestamp not null default CURRENT_TIMESTAMP
);

create table admin_email_tokens_x (
	id varchar(32) primary key default nanoid(),
	token varchar(512) unique not null,
	user_id varchar(32) not null references admin_user_email_accounts(id),
	created_at timestamp not null default CURRENT_TIMESTAMP
);

-- PEER to PEER -------------------------------------------------------------

create table admin_peers (
  person_admin_people_id varchar(32) unique not null references admin_people(id),
  url varchar(128) unique not null,
  peer_approved bool not null default false,
  url_confirmed bool not null default false,
  source_token varchar(64) unique,
  target_token varchar(64) unique,
  session_token varchar(64) unique,

  id varchar(32) primary key default nanoid(),
  sensitivity varchar(32) references common_sensitivity_enum(value) default 'High',
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by_admin_people_id varchar(32) references admin_people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by_admin_people_id varchar(32) references admin_people(id),
  owning_person_admin_people_id varchar(32) references admin_people(id),
  owning_group_admin_groups_id varchar(32) references admin_groups(id)
);

insert into admin_table_name_enum(value) values
  ('admin_database_version_control'),
  ('admin_email_tokens'),
  ('admin_group_memberships'),
  ('admin_group_row_access'),
  ('admin_groups'),
  ('admin_organization_administrators'),
  ('admin_peers'),
  ('admin_people'),
  ('admin_role_all_data_column_grants'),
  ('admin_role_column_grants'),
  ('admin_role_memberships'),
  ('admin_role_table_permissions'),
  ('admin_roles'),
  ('admin_tokens'),
  ('admin_users');




