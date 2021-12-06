-- common schema. org specific schema should go in an org-specific file.

-- ENUMS ----

-- todo
create type common.mime_type as enum (
  'A',
  'B',
  'C'
);

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
  id serial primary key,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.site_text_strings(
  id serial primary key,

  english varchar(64) not null, -- US English, all translations including other English locales will be in the translation table
  comment text,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.site_text_translations(
  id serial primary key,

  language int not null references common.languages(id),
  site_text int not null references common.site_text_strings(id),
  translation varchar(64) not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (language, site_text)
);

create table common.site_text_languages(
  id serial primary key,

  language int not null reference common.languages(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
)

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
  id serial primary key,
  neo4j_id varchar(32) unique,

  book_start common.book_name,
  book_end common.book_name,
  chapter_start int,
  chapter_end int,
  verse_start int,
  verse_end int,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (book_start, book_end, chapter_start, chapter_end, verse_start, verse_end)
);

-- CHAT ------------------------------------------------------------

create table common.discussion_channels (
	id serial primary key,
	name varchar(32) not null,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  unique (name, owning_group)
);

create table common.cell_channels (
	id serial primary key,

  table_name admin.table_name not null,
  column_name varchar(64) not null,
  row int not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (table_name, column_name, row)
);

create table common.threads (
	id serial primary key,

	channel int not null references common.discussion_channels(id) on delete cascade,
	content text not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.posts (
	id serial primary key,
	thread int not null references common.threads(id) on delete cascade,
	content text not null,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- BLOGS ---------------

create table common.blogs (
	id serial primary key,

	title varchar(64) not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (title, owning_group)
);

create table common.blog_posts (
	id serial primary key,

  blog int not null references common.blogs(id),
	content text not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- NOTES ----------------------------------------------------

create table common.notes (
	id serial primary key,

  table_name admin.table_name not null,
  column_name varchar(64) not null,
  row int not null,
	content text not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
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
	id serial primary key,

	name varchar(255) unique, -- not null,
	sensitivity common.sensitivity not null default 'High',
	type common.location_type, -- not null,
	iso_alpha3 char(3) unique,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

ALTER TABLE admin.people ADD CONSTRAINT common_people_primary_location_fk foreign key (primary_location) references common.locations(id);
ALTER TABLE common.locations ADD CONSTRAINT common_locations_created_by_fk foreign key (created_by) references admin.people(id);
ALTER TABLE common.locations ADD CONSTRAINT common_locations_modified_by_fk foreign key (modified_by) references admin.people(id);

-- Education

create table common.education_entries (
  id serial primary key,

  neo4j_id varchar(32) unique,
  degree varchar(64),
  institution varchar(64),
  major varchar(64),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (degree, institution, major)
);

create table common.education_by_person (
  id serial primary key,

  person int not null references admin.people(id),
  education int not null references common.education_entries(id),
  graduation_year int,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (person, education)
);

-- ORGANIZATIONS ------------------------------------------------------------

create table common.organizations (
	id serial primary key,

	name varchar(255) unique, -- not null
	sensitivity common.sensitivity default 'High',
	primary_location int references common.locations(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.org_chart_positions(
  id serial primary key,

  organization int not null references common.organizations(id),
  name varchar(64) not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (organization, name)
);

create type common.position_relationship_types as enum (
  'Reports To',
  'Works With'
);

create table common.org_chart_position_graph(
  id serial primary key,

  from_position int not null references common.org_chart_positions(id),
  to_position int not null references common.org_chart_positions(id),
  relationship_type common.position_relationship_types,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (from_position, to_position, relationship_type)
);

-- COALITIONS ----------------------------------------------------------

create type common.involvement_options as enum (
  'CIT',
  'Engagements'
);

create table common.coalitions(
  id serial primary key,

  name varchar(64) unique not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- coalition memberships

create table common.coalition_memberships(
  id serial primary key,

  coalition int not null references common.coalitions(id),
  organization int not null references common.organizations(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (coalition, organization)
);

-- FILES & DIRECTORIES ----------------------------------------------------------

create table common.directories (
  id serial primary key,
  neo4j_id varchar(32) unique,

  parent int references common.directories(id),
  name varchar(255), -- not null
  
	-- todo
	-- add derived data from sub-directories/files

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.files (
  id serial primary key,
  neo4j_id varchar(32),

  directory int references common.directories(id), --not null
	name varchar(255), -- not null

  -- todo, derived data

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (directory, name)
);

create table common.file_versions (
  id serial primary key,
  neo4j_id varchar(32),

  category varchar(255),
  mime_type varchar(32), -- not null, todo: common.mime_type filled in, but neo4j just has a dumb 'ole string
  name varchar(255), -- not null,
  file int references common.files(id), -- not null
  file_url varchar(255), -- not null,
  file_size int, -- bytes

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- TICKETS ----------------------------------------------------------------------

create type common.ticket_status as enum (
  'Open',
  'Blocked',
  'Closed'
);

create table common.tickets (
	id serial primary key,

	ticket_status common.ticket_status not null default 'Open',
	parent int,
	content text not null,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

ALTER TABLE common.tickets ADD CONSTRAINT common_tickets_parent_fk foreign key (parent) references common.tickets(id);

create table common.ticket_graph (
	id serial primary key,

	from_ticket int not null references common.tickets(id),
	to_ticket int not null references common.tickets(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.ticket_assignments (
	id serial primary key,

	ticket int not null references common.tickets(id),
	person int not null references admin.people(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.work_records(
	id serial primary key,

	person int not null references admin.people(id),
	ticket int not null references common.tickets(id),
	hours int not null,
	minutes int default 0,
	total_time decimal generated always as (
	  hours + (minutes / 60)
	) stored,
	comment text,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.work_estimates(
	id serial primary key,
    ticket int references common.tickets(id),
	person int not null references admin.people(id),
	hours int not null,
	minutes int default 0,
	total_time decimal generated always as (
    hours + (minutes / 60)
  ) stored,
	comment text,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create type common.ticket_feedback_options as enum (
  'Satisfied',
  'Unsatisfied'
);

create table common.ticket_feedback(
	id serial primary key,

	ticket int references common.tickets(id),
	stakeholder int not null references admin.people(id),
	feedback common.ticket_feedback_options not null,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- WORKFLOW -----------------------------------------------------------------

create table common.workflows(
	id serial primary key,

	title varchar(128) not null unique,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.stages(
	id serial primary key,

	title varchar(128) not null unique,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.stage_graph(
	id serial primary key,

	from_stage int not null references common.stages(id),
	to_stage int not null references common.stages(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (from_stage, to_stage)
);

create table common.stage_role_column_grants(
	id serial primary key,

  stage int not null references common.stages(id),
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

create table common.stage_notifications(
	id serial primary key,

	stage int not null references common.stages(id),
	on_enter bool default false,
	on_exit bool default false,
	person int references admin.people(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- PRAYER --------------------------------------------------------------

create table common.prayer_requests(
	id serial primary key,

  parent int references common.prayer_requests(id),
  subject varchar(255) default null,
  content text not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table common.prayer_notifications(
	id serial primary key,

  request int references common.prayer_requests(id),
  person int references admin.people(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
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
  id serial primary key,

	org int not null references common.organizations(id),
	person int not null references admin.people(id),
	relationship_type common.people_to_org_relationship_type,
  begin_at timestamp,
  end_at timestamp,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create type common.people_to_people_relationship_types as enum (
  'Friend',
  'Colleague',
  'Other'
);

create table common.people_graph (
  id serial primary key,

  from_person int not null references admin.people(id),
  to_person int not null references admin.people(id),
  rel_type common.people_to_people_relationship_types not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);
