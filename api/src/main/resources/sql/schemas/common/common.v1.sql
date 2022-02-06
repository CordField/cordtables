-- common schema. org specific schema should go in an org-specific file.

-- ENUMS ----

-- SITE TEXT --------------------------------------------------------------------------------

create type common.egids_scale as enum (
		'0',
		'1',
		'2',
		'3',
		'4',
		'5',
		'6a',
		'6b',
		'7',
		'8a',
		'8b',
		'9',
		'10'
);

-- meant to be extended by all orgs, so everyone has a globally unique id to reference within their language lists
create table common.languages(
  id varchar(32) primary key default common.nanoid(),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.site_text_strings(
  id varchar(32) primary key default common.nanoid(),

  english varchar(64) unique not null, -- US English, all translations including other English locales will be in the translation table
  comment text,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.site_text_translations(
  id varchar(32) primary key default common.nanoid(),

  language varchar(32) not null references common.languages(id),
  site_text varchar(32) not null references common.site_text_strings(id) on delete cascade,
  translation varchar(64) not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

  unique (language, site_text)
);

-- SCRIPTURE REFERENCE -----------------------------------------------------------------

-- todo
create type common.book_name as enum (
  'Genesis',
  'Exodus',
  'Leviticus',
  'Numbers',
  'Deuteronomy',
  'Joshua',
  'Judges',
  'Ruth',
  '1 Samuel',
  '2 Samuel',
  '1 Kings',
  '2 Kings',
  '1 Chronicles',
  '2 Chronicles',
  'Ezra',
  'Nehemiah',
  'Esther',
  'Job',
  'Psalms',
  'Proverbs',
  'Ecclesiastes',
  'The Song of Solomon',
  'Isaiah',
  'Jeremiah',
  'Lamentations',
  'Ezekiel',
  'Daniel',
  'Hosea',
  'Joel',
  'Amos',
  'Obadiah',
  'Jonah',
  'Micah',
  'Nahum',
  'Habakkuk',
  'Zephaniah',
  'Haggai',
  'Zechariah',
  'Malachi',
  'Matthew',
  'Mark',
  'Luke',
  'John',
  'Acts',
  'Romans',
  '1 Corinthians',
  '2 Corinthians',
  'Galatians',
  'Ephesians',
  'Philippians',
  'Colossians',
  '1 Thessalonians',
  '2 Thessalonians',
  '1 Timothy',
  '2 Timothy',
  'Titus',
  'Philemon',
  'Hebrews',
  'James',
  '1 Peter',
  '2 Peter',
  '1 John',
  '2 John',
  '3 John',
  'Jude',
  'Revelation'
);

create table common.scripture_references (
  id varchar(32) primary key default common.nanoid(),

  book_start common.book_name,
  book_end common.book_name,
  chapter_start int,
  chapter_end int,
  verse_start int,
  verse_end int,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

-- CHAT ------------------------------------------------------------

create table common.discussion_channels (
	id varchar(32) primary key default common.nanoid(),
	name varchar(32) not null,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),
  unique (name, owning_group)
);

create table common.cell_channels (
	id varchar(32) primary key default common.nanoid(),

  table_name admin.table_name not null,
  column_name varchar(64) not null,
  row varchar(32) not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

  unique (table_name, column_name, row)
);

create table common.threads (
	id varchar(32) primary key default common.nanoid(),

	channel varchar(32) not null references common.discussion_channels(id) on delete cascade,
	content text not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.posts (
	id varchar(32) primary key default common.nanoid(),
	thread varchar(32) not null references common.threads(id) on delete cascade,
	content text not null,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

-- BLOGS ---------------

create table common.blogs (
	id varchar(32) primary key default common.nanoid(),

	title varchar(64) not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

  unique (title, owning_group)
);

create table common.blog_posts (
	id varchar(32) primary key default common.nanoid(),

  blog varchar(32) not null references common.blogs(id),
	content text not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

-- NOTES ----------------------------------------------------

create table common.notes (
	id varchar(32) primary key default common.nanoid(),

  table_name admin.table_name not null,
  column_name varchar(64) not null,
  row varchar(32) not null,
	content text not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

-- LOCATION -----------------------------------------------------------------

create type common.location_type as enum (
  'City',
  'County',
  'State',
  'Country',
  'CrossBorderArea'
);

create table common.locations (
	id varchar(32) primary key default common.nanoid(),

	name varchar(255) unique, -- not null,
	sensitivity common.sensitivity not null default 'High',
	type common.location_type, -- not null,
	iso_alpha3 char(3) unique,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by varchar(32) not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

ALTER TABLE admin.people ADD CONSTRAINT common_people_primary_location_fk foreign key (primary_location) references common.locations(id);
ALTER TABLE common.locations ADD CONSTRAINT common_locations_created_by_fk foreign key (created_by) references admin.people(id);
ALTER TABLE common.locations ADD CONSTRAINT common_locations_modified_by_fk foreign key (modified_by) references admin.people(id);

-- Education

create table common.education_entries (
  id varchar(32) primary key default common.nanoid(),

  degree varchar(64),
  institution varchar(64),
  major varchar(64),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

  unique (degree, institution, major)
);

create table common.education_by_person (
  id varchar(32) primary key default common.nanoid(),

  person varchar(32) unique not null references admin.people(id),
  education varchar(32) not null references common.education_entries(id),
  graduation_year int,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

  unique (person, education)
);

-- ORGANIZATIONS ------------------------------------------------------------

create table common.organizations (
	id varchar(32) primary key default common.nanoid(),

	name varchar(255) unique, -- not null
	sensitivity common.sensitivity default 'High',
	primary_location varchar(32) references common.locations(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.org_chart_positions(
  id varchar(32) primary key default common.nanoid(),

  organization varchar(32) not null references common.organizations(id),
  name varchar(64) not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

  unique (organization, name)
);

create type common.position_relationship_types as enum (
  'Reports To',
  'Works With'
);

create table common.org_chart_position_graph(
  id varchar(32) primary key default common.nanoid(),

  from_position varchar(32) not null references common.org_chart_positions(id),
  to_position varchar(32) not null references common.org_chart_positions(id),
  relationship_type common.position_relationship_types,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

  unique (from_position, to_position, relationship_type)
);

-- COALITIONS ----------------------------------------------------------

create type common.involvement_options as enum (
  'CIT',
  'Engagements'
);

create table common.coalitions(
  id varchar(32) primary key default common.nanoid(),

  name varchar(64) unique not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

-- coalition memberships

create table common.coalition_memberships(
  id varchar(32) primary key default common.nanoid(),

  coalition varchar(32) not null references common.coalitions(id),
  organization varchar(32) not null references common.organizations(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

  unique (coalition, organization)
);

-- FILES & DIRECTORIES ----------------------------------------------------------

create table common.directories (
  id varchar(32) primary key default common.nanoid(),

  parent varchar(32) references common.directories(id),
  name varchar(255), -- not null
  
	-- todo
	-- add derived data from sub-directories/files

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.files (
  id varchar(32) primary key default common.nanoid(),

  directory varchar(32) references common.directories(id), --not null
	name varchar(255), -- not null

  -- todo, derived data

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.file_versions (
  id varchar(32) primary key default common.nanoid(),

  mime_type varchar(128),
  name varchar(255), -- not null,
  file varchar(32) references common.files(id), -- not null
  file_url varchar(255), -- not null,
  file_size int, -- bytes

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

-- TICKETS ----------------------------------------------------------------------

create type common.ticket_status as enum (
  'Open',
  'Blocked',
  'Closed'
);

create table common.tickets (
	id varchar(32) primary key default common.nanoid(),
  title varchar(64) not null,
	ticket_status common.ticket_status not null default 'Open',
	parent varchar(32),
	content text not null,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

ALTER TABLE common.tickets ADD CONSTRAINT common_tickets_parent_fk foreign key (parent) references common.tickets(id);

create table common.ticket_graph (
	id varchar(32) primary key default common.nanoid(),

	from_ticket varchar(32) not null references common.tickets(id),
	to_ticket varchar(32) not null references common.tickets(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.ticket_assignments (
	id varchar(32) primary key default common.nanoid(),

	ticket varchar(32) not null references common.tickets(id),
	person varchar(32) not null references admin.people(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.work_records(
	id varchar(32) primary key default common.nanoid(),

	person varchar(32) not null references admin.people(id),
	ticket varchar(32) not null references common.tickets(id),
	hours int not null,
	minutes int default 0,
	total_time decimal generated always as (
	  hours + (minutes / 60)
	) stored,
	comment text,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.work_estimates(
	id varchar(32) primary key default common.nanoid(),
    ticket varchar(32) references common.tickets(id),
	person varchar(32) not null references admin.people(id),
	hours int not null,
	minutes int default 0,
	total_time decimal generated always as (
    hours + (minutes / 60)
  ) stored,
	comment text,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create type common.ticket_feedback_options as enum (
  'Satisfied',
  'Unsatisfied'
);

create table common.ticket_feedback(
	id varchar(32) primary key default common.nanoid(),

	ticket varchar(32) references common.tickets(id),
	stakeholder varchar(32) not null references admin.people(id),
	feedback common.ticket_feedback_options not null,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

-- WORKFLOW -----------------------------------------------------------------

create table common.workflows(
	id varchar(32) primary key default common.nanoid(),

	title varchar(128) not null unique,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.stages(
	id varchar(32) primary key default common.nanoid(),

	title varchar(128) not null unique,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create table common.stage_graph(
	id varchar(32) primary key default common.nanoid(),

	from_stage varchar(32) not null references common.stages(id),
	to_stage varchar(32) not null references common.stages(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

  unique (from_stage, to_stage)
);

create table common.stage_role_column_grants(
	id varchar(32) primary key default common.nanoid(),

  stage varchar(32) not null references common.stages(id),
	role varchar(32) not null references admin.roles(id),
	table_name admin.table_name not null,
	column_name varchar(64) not null,
	access_level admin.access_level not null,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by varchar(32) not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id),

	unique (role, table_name, column_name)
);

create table common.stage_notifications(
	id varchar(32) primary key default common.nanoid(),

	stage varchar(32) not null references common.stages(id),
	on_enter bool default false,
	on_exit bool default false,
	person varchar(32) unique references admin.people(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

-- SOCIAL GRAPH ----------------------------------------------------

create type common.people_to_org_relationship_type as enum (
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

create table common.people_to_org_relationships (
  id varchar(32) primary key default common.nanoid(),

	org varchar(32) not null references common.organizations(id),
	person varchar(32) unique not null references admin.people(id),
	relationship_type common.people_to_org_relationship_type,
  begin_at timestamp,
  end_at timestamp,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);

create type common.people_to_people_relationship_types as enum (
  'Friend',
  'Colleague',
  'Other'
);

create table common.people_graph (
  id varchar(32) primary key default common.nanoid(),

  from_person varchar(32) unique not null references admin.people(id),
  to_person varchar(32) unique not null references admin.people(id),
  rel_type common.people_to_people_relationship_types not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by varchar(32) not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by varchar(32) not null references admin.people(id),
  owning_person varchar(32) not null references admin.people(id),
  owning_group varchar(32) not null references admin.groups(id)
);
