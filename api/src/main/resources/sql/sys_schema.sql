-- system schema. org specific schema should go in an org-specific file.

-- ENUMS ----
create schema if not exists public;

set schema 'public';
CREATE EXTENSION hstore;

DO $$ BEGIN
    create type public.access_level as enum (
          'Read',
          'Write',
          'Admin'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.mime_type as enum (
          'A',
          'B',
          'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;
create type public.toggle_security as enum(
    'NoSecurity',
    'UpdateAccessLevelSecurity',
    'UpdateAccessLevelAndIsClearedSecurity'
);
create type public.toggle_sensitivity as enum('UpdateIsCleared', 'DontUpdateIsCleared');

create type public.toggle_mv as enum(
    'NoRefreshMV',
    'RefreshMV',
    'RefreshMVConcurrently'
);

create type public.toggle_history as enum(
    'NoHistory',
    'History'
);

create type public.toggle_granters as enum(
    'NoRefresh',
    'RefreshSecurityTables',
    'RefreshSecurityTablesAndMV',
    'RefreshSecurityTablesAndMVConcurrently'
);

DO $$ BEGIN
    create type public.sensitivity as enum (
		'Low',
		'Medium',
		'High'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

DO $$ BEGIN
    create type public.post_type as enum (
		'Note',
		'Story',
		'Prayer'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

DO $$ BEGIN
    create type public.post_shareability as enum (
		'Project Team',
		'Internal',
		'Ask to Share Externally',
		'External'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

DO $$ BEGIN
    create type public.periodic_report_type as enum (
		'Financial',
		'Narrative'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- ROLES --------------------------------------------------------------------

create table if not exists public.global_roles (
	id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
    modified_by int not null default 1,
	name varchar(255) not null,
	org int,
	unique (org, name)
-- foreign keys added after people and org table created
);

DO $$ BEGIN
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
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

DO $$ BEGIN
    create type public.table_permission as enum (
		'Create',
		'Delete'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists public.global_role_column_grants(
	id serial primary key,
	access_level access_level not null,
	column_name varchar(64) not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	global_role int not null,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
    modified_by int not null default 1,
	table_name public.table_name not null,
	unique (global_role, table_name, column_name),
    -- foreign keys added after people table created
	foreign key (global_role) references public.global_roles(id)
);

create table if not exists public.global_role_table_permissions(
    id serial primary key,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    created_by int not null default 1,
    global_role int not null,
    modified_at timestamp not null default CURRENT_TIMESTAMP,
    modified_by int not null default 1,
    table_name varchar(32) not null,
    table_permission table_permission not null,
    unique (global_role, table_name, table_permission),
-- foreign keys added after people table created
    foreign key (global_role) references public.global_roles(id)
);

create table if not exists public.global_role_memberships (
    id serial primary key,
	global_role int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
    modified_by int not null default 1,
	person int,
	unique(global_role,person),
--	foreign key (created_by) references public.people(id), -- fk added later
	foreign key (global_role) references public.global_roles(id)
);

-- SCRIPTURE REFERENCE -----------------------------------------------------------------

-- todo
DO $$ BEGIN
    create type public.book_name as enum (
          'Genesis',
          'Matthew',
          'Revelation'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists public.scripture_references (
    id serial primary key,
    book_start book_name,
    book_end book_name,
    chapter_start int,
    chapter_end int,
    verse_start int,
    verse_end int,
    unique (book_start, book_end, chapter_start, chapter_end, verse_start, verse_end)
);

-- LOCATION -----------------------------------------------------------------

DO $$ BEGIN
    create type public.location_type as enum (
          'City',
          'County',
          'State',
		  'Country',
          'CrossBorderArea'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists public.locations (
	id serial primary key,
	neo4j_id varchar(32),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	chat_id int not null,
	name varchar(255) unique not null,
	sensitivity sensitivity not null default 'High',
	type location_type not null
	-- foreign key(chat_id) references public.chats(id)
-- foreign keys added after people table created
);


-- LANGUAGE -----------------------------------------------------------------

-- sil tables are copied from SIL schema docs
-- https://www.ethnologue.com/codes/code-table-structure
-- http://www.ethnologue.com/sites/default/files/Ethnologue-19-Global%20Dataset%20Doc.pdf

create schema if not exists sil;

CREATE TABLE if not exists sil.language_codes (
   lang char(3) not null,  -- Three-letter code
   country char(2) not null,  -- Main country where used
   lang_status char(1) not null,  -- L(iving), (e)X(tinct)
   name varchar(75) not null   -- Primary name in that country
);

CREATE TABLE if not exists sil.country_codes (
   country char(2) not null,  -- Two-letter code from ISO3166
   name varchar(75) not null,  -- Country name
   area varchar(10) not null -- World area
);

CREATE TABLE if not exists sil.language_index (
   lang char(3) not null,  -- Three-letter code for language
   country char(2) not null,  -- Country where this name is used
   name_type char(2) not null,  -- L(anguage), LA(lternate),
                                -- D(ialect), DA(lternate)
                                -- LP,DP (a pejorative alternate)
   name  varchar(75) not null
);



-- PEOPLE ------------------------------------------------------------

create table if not exists public.people (
    id serial primary key,
	neo4j_id varchar(32),
    about text,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    created_by int,
    modified_at timestamp not null default CURRENT_TIMESTAMP,
    modified_by int,
    phone varchar(32),
	picture varchar(255),
    primary_org int,
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
    foreign key (created_by) references public.people(id),
    foreign key (modified_by) references public.people(id),
-- foreign keys added after org table created
    foreign key (primary_location) references public.locations(id)
);
create table if not exists public.chats (
	id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	foreign key (created_by) references public.people(id)
);

create table if not exists public.posts (
	id serial primary key,
	content text not null,
	chat_id int not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
	is_modified bool not null default false,
	foreign key (created_by) references public.people(id)
	-- foreign key (chat_id) references public.chats(id)
);

create table if not exists sil.table_of_languages (
  id serial primary key,
  sil_ethnologue_legacy varchar(32),
	iso_639 char(3),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	code varchar(32),
	language_name varchar(50) not null,
	population int,
	chat_id int not null,
	provisional_code varchar(32)
	-- foreign key(chat_id) references public.chats(id)
);

-- fkey for a bunch of stuff
DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_roles_created_by_fk') THEN
ALTER TABLE public.global_roles ADD CONSTRAINT public_global_roles_created_by_fk foreign key (created_by) references public.people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_roles_modified_by_fk') THEN
ALTER TABLE public.global_roles ADD CONSTRAINT public_global_roles_modified_by_fk foreign key (modified_by) references people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_role_grants_created_by_fk') THEN
ALTER TABLE public.global_role_column_grants ADD CONSTRAINT public_global_role_grants_created_by_fk foreign key (created_by) references public.people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_role_grants_modified_by_fk') THEN
ALTER TABLE public.global_role_column_grants ADD CONSTRAINT public_global_role_grants_modified_by_fk foreign key (modified_by) references people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_role_table_permissions _created_by_fk') THEN
ALTER TABLE public.global_role_table_permissions ADD CONSTRAINT public_global_role_table_permissions_created_by_fk foreign key (created_by) references public.people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_role_table_permissions_modified_by_fk') THEN
ALTER TABLE public.global_role_table_permissions ADD CONSTRAINT public_global_role_table_permissions_modified_by_fk foreign key (modified_by) references people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_role_memberships_person_fk') THEN
ALTER TABLE public.global_role_memberships ADD CONSTRAINT public_global_role_memberships_person_fk foreign key (person) references public.people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_role_memberships_created_by_fk') THEN
ALTER TABLE public.global_role_memberships ADD CONSTRAINT public_global_role_memberships_created_by_fk foreign key (created_by) references public.people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_role_memberships_modified_by_fk') THEN
ALTER TABLE public.global_role_memberships ADD CONSTRAINT public_global_role_memberships_modified_by_fk foreign key (modified_by) references people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_locations_created_by_fk') THEN
ALTER TABLE public.locations ADD CONSTRAINT public_locations_created_by_fk foreign key (created_by) references public.people(id);
END IF; END; $$;

DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_locations_modified_by_fk') THEN
ALTER TABLE public.locations ADD CONSTRAINT public_locations_modified_by_fk foreign key (modified_by) references people(id);
END IF; END; $$;

-- Education

create table if not exists public.education_entries (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null,
  degree varchar(64),
  institution varchar(64),
  major varchar(64),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id)
);

create table if not exists public.education_by_person (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
  education int not null,
  graduation_year int,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  person int not null,
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (person) references public.people(id),
	foreign key (education) references public.education_entries(id)
);

-- ORGANIZATIONS ------------------------------------------------------------

create table if not exists public.organizations (
	id serial primary key,
	neo4j_id varchar(32),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	name varchar(255) unique not null,
	sensitivity sensitivity default 'High',
	primary_location int,
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (primary_location) references locations(id)
);


DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'public_global_roles_org_fk') THEN
ALTER TABLE public.global_roles ADD CONSTRAINT public_global_roles_org_fk foreign key (org) references organizations(id);
END IF; END; $$;

-- fkey for people
DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'primary_org_fkey') THEN
ALTER TABLE public.people ADD CONSTRAINT primary_org_fkey foreign key (primary_org) references public.organizations(id);
END IF; END; $$;

-- fkey for global_roles
DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'global_role_org_fkey') THEN
ALTER TABLE global_roles ADD CONSTRAINT global_role_org_fkey foreign key (org) references public.organizations(id);
END IF; END; $$;

DO $$ BEGIN
    create type public.person_to_org_relationship_type as enum (
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

create table if not exists public.organization_grants(
    id serial primary key,
    access_level access_level not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    created_by int not null default 1,
    column_name varchar(32) not null,
    modified_at timestamp not null default CURRENT_TIMESTAMP,
    modified_by int not null default 1,
    org int not null,
    table_name table_name not null,
    unique (org, table_name, column_name, access_level),
    foreign key (created_by) references public.people(id),
    foreign key (modified_by) references public.people(id),
    foreign key (org) references organizations(id)
);

create table if not exists public.organization_memberships(
    id serial primary key,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    created_by int not null default 1,
    modified_at timestamp not null default CURRENT_TIMESTAMP,
    modified_by int not null default 1,
    org int not null,
    person int not null,
    foreign key (created_by) references public.people(id),
    foreign key (modified_by) references public.people(id),
    foreign key (org) references organizations(id),
    foreign key (person) references people(id)
);

create table if not exists public.people_to_org_relationships (
  id serial primary key,
	org int,
	person int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (org) references organizations(id),
	foreign key (person) references people(id)
);

create table if not exists public.people_to_org_relationship_type (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
  begin_at timestamp not null,
	end_at timestamp,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  people_to_org int,
	relationship_type person_to_org_relationship_type,
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (people_to_org) references people_to_org_relationships(id)
);

-- USERS ---------------------------------------------------------------------

create table if not exists public.users(
  id serial primary key,
	person int not null,
	owning_org int not null,
	email varchar(255) unique not null,
	password varchar(255) not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (person) references public.people(id),
	foreign key (owning_org) references public.organizations(id)
);

-- GROUPS --------------------------------------------------------------------

create table if not exists public.groups(
  id serial primary key,
  name int not null,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  unique (name),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id)
);

create table if not exists public.group_row_access(
  id serial primary key,
  group_id int not null,
  table_name table_name not null,
  row int not null,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  foreign key (group_id) references public.groups(id),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id)
);

-- PROJECTS ------------------------------------------------------------------

create table if not exists public.projects (
	id serial primary key,
	neo4j_id varchar(32),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	name varchar(32) not null,
	primary_org int,
	primary_location int,
	sensitivity sensitivity default 'High',
	chat_id int not null,
	unique (primary_org, name),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (primary_org) references organizations(id),
	foreign key (primary_location) references locations(id)
	-- foreign key (chat_id) references public.chats(id)
);

create table if not exists public.project_memberships (
  id serial primary key,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null default 1,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  person int not null,
  project int not null,
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (project) references projects(id),
  foreign key (person) references people(id)
);

create table if not exists public.project_roles (
	id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	name varchar(255) not null,
	org int,
	unique (org, name),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (org) references public.organizations(id)
);

create table if not exists public.project_role_column_grants (
  id serial primary key,
	access_level access_level not null,
	column_name varchar(32) not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
	project_role int not null,
	table_name table_name not null,
	unique (project_role, table_name, column_name, access_level),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (project_role) references project_roles(id)
);

create table if not exists public.project_member_roles (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 1,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 1,
  person int not null,
  project int not null,
	project_role int,
	unique (project, person),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (person) references people(id),
	foreign key (project) references projects(id),
	foreign key (project_role) references project_roles(id)
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

-- TICKETS ----------------------------------------------------------------------
--
--DO $$ BEGIN
--    create type public.ticket_status as enum (
--          'Open',
--					'Blocked',
--					'Closed'
--	);
--	EXCEPTION
--	WHEN duplicate_object THEN null;
--END; $$;
--
--create table if not exists public.tickets (
--	id serial primary key,
--	ticket_status public.ticket_status not null default 'Open',
--	title varchar(125) not null,
--	chat_id int,
--	work_orders int,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references public.people(id),
--	foreign key (chat_id) references public.chats(id),
--	foreign key (work_orders) references public.work_order_lists(id)
--);
--
--create table if not exists public.work_orders(
--	id serial primary key,
--	ticket_id int,
--	content text not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references public.people(id),
--	foreign key (ticket_id) references public.ticket(id)
--);
--
--create table if not exists public.ticket_assignments (
--	id serial primary key,
--	ticket_id int not null,
--	person_id int not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references public.people(id),
--	foreign key (ticket_id) references public.tickets(id),
--	foreign key (person_id) references public.people(id)
--);
--
--create table if not exists public.work_records(
--	id serial primary key,
--	person int not null,
--	hours decimal not null,
--	comment text,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (person) references public.people(id),
--	foreign key (created_by) references public.people(id)
--);
--
--create table if not exists public.work_estimates(
--	id serial primary key,
--	person int not null,
--	hours decimal not null,
--	comment text,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (person) references public.people(id),
--	foreign key (created_by) references public.people(id)
--);
--
--create table if not exists public.ticket_feedback(
--	id serial primary key,
--	ticket_id int,
--	stakeholder int not null,
--	content text not null,
--	chat_id int,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references public.people(id),
--	foreign key (stakeholder) references public.people(id),
--	foreign key (chat_id) references public.chats(id),
--	foreign key (ticket_id) references public.ticket(id)
--);
--
---- WORKFLOW -----------------------------------------------------------------
--
--create table if not exists public.workflows(
--	id serial primary key,
--	title varchar(128) not null,
--	chat_id int,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (chat_id) references public.chats(id),
--	foreign key (created_by) references public.people(id)
--);
--
--create table if not exists public.stages(
--	id serial primary key,
--	workflow_id int not null,
--	title varchar(128) not null,
--	chat_id int,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references public.people(id),
--	foreign key (chat_id) references public.chats(id),
--	foreign key (workflow_id) references public.workflows(id)
--);
--
--create table if not exists public.work_order_templates(
--	id serial primary key,
--	stage_id int,
--	content text not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references public.people(id),
--	foreign key (stage_id) references public.stages(id)
--);
--
--create table if not exists public.stage_options(
--	id serial primary key,
--	from_stage_id int not null,
--	to_stage_id int not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references public.people(id),
--	foreign key (from_stage_id) references public.stages(id),
--	foreign key (to_stage_id) references public.stages(id)
--);
--
--create table if not exists public.stage_notifications(
--	id serial primary key,
--	stage_id int not null,
--	email varchar(64) not null,
--	created_at timestamp not null default CURRENT_TIMESTAMP,
--	created_by int not null default 1,
--	foreign key (created_by) references public.people(id),
--	foreign key (to_stage_id) references public.stages(id)
--);