-- system schema. org specific schema should go in an org-specific file.

-- ENUMS ----

-- todo
DO $$ BEGIN
    create type common.mime_type as enum (
          'A',
          'B',
          'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;
create type common.toggle_security as enum(
    'NoSecurity',
    'UpdateAccessLevelSecurity',
    'UpdateAccessLevelAndIsClearedSecurity'
);
create type common.toggle_sensitivity as enum('UpdateIsCleared', 'DontUpdateIsCleared');

create type common.toggle_mv as enum(
    'NoRefreshMV',
    'RefreshMV',
    'RefreshMVConcurrently'
);

create type common.toggle_history as enum(
    'NoHistory',
    'History'
);

create type common.toggle_granters as enum(
    'NoRefresh',
    'RefreshSecurityTables',
    'RefreshSecurityTablesAndMV',
    'RefreshSecurityTablesAndMVConcurrently'
);

DO $$ BEGIN
    create type common.post_type as enum (
		'Note',
		'Story',
		'Prayer'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

DO $$ BEGIN
    create type common.post_shareability as enum (
		'Project Team',
		'Internal',
		'Ask to Share Externally',
		'External'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

DO $$ BEGIN
    create type common.periodic_report_type as enum (
		'Financial',
		'Narrative'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- SCRIPTURE REFERENCE -----------------------------------------------------------------

-- todo
DO $$ BEGIN
    create type common.book_name as enum (
          'Genesis',
          'Matthew',
          'Revelation'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists common.scripture_references (
    id serial primary key,
    book_start common.book_name,
    book_end common.book_name,
    chapter_start int,
    chapter_end int,
    verse_start int,
    verse_end int,
    unique (book_start, book_end, chapter_start, chapter_end, verse_start, verse_end)
);

-- LOCATION -----------------------------------------------------------------

DO $$ BEGIN
    create type common.location_type as enum (
          'City',
          'County',
          'State',
		  'Country',
          'CrossBorderArea'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists common.locations (
	id serial primary key,
	neo4j_id varchar(32),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	chat_id int not null,
	name varchar(255) unique not null,
	sensitivity common.sensitivity not null default 'High',
	type location_type not null
	-- foreign key(chat_id) references common.chats(id)
-- foreign keys added after people table created
);

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'common_people_primary_location_fk') THEN
ALTER TABLE admin.people ADD CONSTRAINT common_people_primary_location_fk foreign key (primary_location) references common.locations(id);
END IF; END; $$;

-- fkey for a bunch of stuff
DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'common_locations_created_by_fk') THEN
ALTER TABLE common.locations ADD CONSTRAINT common_locations_created_by_fk foreign key (created_by) references admin.people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'common_locations_modified_by_fk') THEN
ALTER TABLE common.locations ADD CONSTRAINT common_locations_modified_by_fk foreign key (modified_by) references admin.people(id);
END IF; END; $$;

-- CHAT ------------------------------------------------------------

create table if not exists common.chats (
	id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	foreign key (created_by) references admin.people(id)
);

create table if not exists common.posts (
	id serial primary key,
	content text not null,
	chat_id int not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
	is_modified bool not null default false,
	foreign key (created_by) references admin.people(id)
	-- foreign key (chat_id) references common.chats(id)
);

-- Education

create table if not exists common.education_entries (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null,
  degree varchar(64),
  institution varchar(64),
  major varchar(64),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id)
);

create table if not exists common.education_by_person (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
  education int not null,
  graduation_year int,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  person int not null,
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
	foreign key (person) references admin.people(id),
	foreign key (education) references common.education_entries(id)
);

-- ORGANIZATIONS ------------------------------------------------------------

create table if not exists common.organizations (
	id serial primary key,

	name varchar(255) unique not null,
	neo4j_id varchar(32),
	sensitivity common.sensitivity default 'High',
	primary_location int,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

	foreign key (primary_location) references locations(id),
	foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);

DO $$ BEGIN
    create type common.person_to_org_relationship_type as enum (
          'Vendor',
          'Customer',
          'Investor',
          'Associate',
          'Employee',
          'Member',
		  'Executive',
		  'President/CEO',
          'Board of Directors',
          'Retired',
          'Other'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

--create table if not exists common.organization_grants(
--    id serial primary key,
--    access_level admin.access_level not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    created_by int not null default 1,
--    column_name varchar(32) not null,
--    modified_at timestamp not null default CURRENT_TIMESTAMP,
--    modified_by int not null default 1,
--    org int not null,
--    table_name admin.table_name not null,
--    unique (org, table_name, column_name, access_level),
--    foreign key (created_by) references admin.people(id),
--    foreign key (modified_by) references admin.people(id),
--    foreign key (org) references organizations(id)
--);
--
--create table if not exists common.organization_memberships(
--    id serial primary key,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    created_by int not null default 1,
--    modified_at timestamp not null default CURRENT_TIMESTAMP,
--    modified_by int not null default 1,
--    org int not null,
--    person int not null,
--    foreign key (created_by) references admin.people(id),
--    foreign key (modified_by) references admin.people(id),
--    foreign key (org) references organizations(id),
--    foreign key (person) references admin.people(id)
--);

create table if not exists admin.people_to_org_relationships (
  id serial primary key,
	org int,
	person int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
	foreign key (org) references organizations(id),
	foreign key (person) references admin.people(id)
);

create table if not exists admin.people_to_org_relationship_type (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
  begin_at timestamp not null,
	end_at timestamp,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  people_to_org int,
	relationship_type person_to_org_relationship_type,
	foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
	foreign key (people_to_org) references admin.people_to_org_relationships(id)
);

-- USERS ---------------------------------------------------------------------

create table if not exists common.users(
  id serial primary key,

	person int not null,
	email varchar(255) unique not null,
	password varchar(255) not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

	foreign key (person) references admin.people(id),
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);



-- PROJECTS ------------------------------------------------------------------

create table if not exists common.projects (
	id serial primary key,
	neo4j_id varchar(32),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	name varchar(32) not null,
	primary_org int,
	primary_location int,
	sensitivity common.sensitivity default 'High',
	chat_id int not null,
	unique (primary_org, name),
	foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
	foreign key (primary_org) references organizations(id),
	foreign key (primary_location) references locations(id)
	-- foreign key (chat_id) references common.chats(id)
);

create table if not exists common.project_memberships (
  id serial primary key,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null default 1,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  person int not null,
  project int not null,
  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
  foreign key (project) references projects(id),
  foreign key (person) references admin.people(id)
);

create table if not exists common.project_roles (
	id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	name varchar(255) not null,
	org int,
	unique (org, name),
	foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
	foreign key (org) references common.organizations(id)
);

create table if not exists common.project_role_column_grants (
  id serial primary key,
	access_level admin.access_level not null,
	column_name varchar(32) not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	project_role int not null,
	table_name admin.table_name not null,
	unique (project_role, table_name, column_name, access_level),
	foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
	foreign key (project_role) references project_roles(id)
);

create table if not exists common.project_member_roles (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  person int not null,
  project int not null,
	project_role int,
	unique (project, person),
	foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
	foreign key (person) references admin.people(id),
	foreign key (project) references projects(id),
	foreign key (project_role) references project_roles(id)
);

-- email tokens

create table if not exists common.email_tokens (
	id serial primary key,
	token varchar(512),
	email varchar(255),
	unique(token),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	foreign key (email) references users(email)
);

-- TICKETS ----------------------------------------------------------------------
--
--DO $$ BEGIN
--    create type common.ticket_status as enum (
--          'Open',
--					'Blocked',
--					'Closed'
--	);
--	EXCEPTION
--	WHEN duplicate_object THEN null;
--END; $$;
--
--create table if not exists common.tickets (
--	id serial primary key,
--	ticket_status common.ticket_status not null default 'Open',
--	title varchar(125) not null,
--	chat_id int,
--	work_orders int,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references admin.people(id),
--	foreign key (chat_id) references common.chats(id),
--	foreign key (work_orders) references common.work_order_lists(id)
--);
--
--create table if not exists common.work_orders(
--	id serial primary key,
--	ticket_id int,
--	content text not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references admin.people(id),
--	foreign key (ticket_id) references common.ticket(id)
--);
--
--create table if not exists common.ticket_assignments (
--	id serial primary key,
--	ticket_id int not null,
--	person_id int not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references admin.people(id),
--	foreign key (ticket_id) references common.tickets(id),
--	foreign key (person_id) references admin.people(id)
--);
--
--create table if not exists common.work_records(
--	id serial primary key,
--	person int not null,
--	hours decimal not null,
--	comment text,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (person) references admin.people(id),
--	foreign key (created_by) references admin.people(id)
--);
--
--create table if not exists common.work_estimates(
--	id serial primary key,
--	person int not null,
--	hours decimal not null,
--	comment text,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (person) references admin.people(id),
--	foreign key (created_by) references admin.people(id)
--);
--
--create table if not exists common.ticket_feedback(
--	id serial primary key,
--	ticket_id int,
--	stakeholder int not null,
--	content text not null,
--	chat_id int,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references admin.people(id),
--	foreign key (stakeholder) references admin.people(id),
--	foreign key (chat_id) references common.chats(id),
--	foreign key (ticket_id) references common.ticket(id)
--);
--
---- WORKFLOW -----------------------------------------------------------------
--
--create table if not exists common.workflows(
--	id serial primary key,
--	title varchar(128) not null,
--	chat_id int,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (chat_id) references common.chats(id),
--	foreign key (created_by) references admin.people(id)
--);
--
--create table if not exists common.stages(
--	id serial primary key,
--	workflow_id int not null,
--	title varchar(128) not null,
--	chat_id int,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references admin.people(id),
--	foreign key (chat_id) references common.chats(id),
--	foreign key (workflow_id) references common.workflows(id)
--);
--
--create table if not exists common.work_order_templates(
--	id serial primary key,
--	stage_id int,
--	content text not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references admin.people(id),
--	foreign key (stage_id) references common.stages(id)
--);
--
--create table if not exists common.stage_options(
--	id serial primary key,
--	from_stage_id int not null,
--	to_stage_id int not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references admin.people(id),
--	foreign key (from_stage_id) references common.stages(id),
--	foreign key (to_stage_id) references common.stages(id)
--);
--
--create table if not exists common.stage_notifications(
--	id serial primary key,
--	stage_id int not null,
--	email varchar(64) not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references admin.people(id),
--	foreign key (to_stage_id) references common.stages(id)
--);