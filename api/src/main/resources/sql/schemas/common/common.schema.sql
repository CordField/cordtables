-- common schema. org specific schema should go in an org-specific file.

-- ENUMS ----

-- todo
create type common.mime_type as enum (
  'A',
  'B',
  'C'
);

-- SITE TEXT --------------------------------------------------------------------------------

create table common.site_text(
  id serial primary key,

  ethnologue int not null,

  cord_tables varchar(32),
  edit_mode varchar(32),
  email_address varchar(32),
  false_ varchar(32),
  home varchar(32),
  languages varchar(32),
  login varchar(32),
  logout varchar(32),
  password varchar(32),
  please_login_or_register varchar(128),
  profile_page varchar(32),
  register varchar(32),
  sc_languages varchar(32),
  true_ varchar(32),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

-- SCRIPTURE REFERENCE -----------------------------------------------------------------

-- todo
create type common.book_name as enum (
  'Genesis',
  'Matthew',
  'Revelation'
);

create table common.scripture_references (
  id serial primary key,

  book_start common.book_name,
  book_end common.book_name,
  chapter_start int,
  chapter_end int,
  verse_start int,
  verse_end int,

  unique (book_start, book_end, chapter_start, chapter_end, verse_start, verse_end),
  peer int references admin.peers(id)
);

-- CHAT ------------------------------------------------------------

create table common.discussion_channels (
	id serial primary key,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
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
  peer int references admin.peers(id)
);

create table common.threads (
	id serial primary key,

	channel int not null references common.discussion_channels(id),
	content text not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.posts (
	id serial primary key,

	thread int not null references common.threads(id),
	content text not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
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

	name varchar(255) unique not null,
	sensitivity common.sensitivity not null default 'High',
	type common.location_type not null,

	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

ALTER TABLE admin.people ADD CONSTRAINT common_people_primary_location_fk foreign key (primary_location) references common.locations(id);
ALTER TABLE common.locations ADD CONSTRAINT common_locations_created_by_fk foreign key (created_by) references admin.people(id);
ALTER TABLE common.locations ADD CONSTRAINT common_locations_modified_by_fk foreign key (modified_by) references admin.people(id);

-- Education

create table common.education_entries (
  id serial primary key,

  degree varchar(64),
  institution varchar(64),
  major varchar(64),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
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
  peer int references admin.peers(id)
);

-- ORGANIZATIONS ------------------------------------------------------------

create table common.organizations (
	id serial primary key,

	name varchar(255) unique not null,
	sensitivity common.sensitivity default 'High',
	primary_location int references common.locations(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

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

create table common.people_to_org_relationships (
  id serial primary key,

	org int not null references common.organizations(id),
	person int not null references admin.people(id),
	
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.people_to_org_relationship_type (
  id serial primary key,

  begin_at timestamp not null,
	end_at timestamp,
  people_to_org int not null,
	relationship_type int not null references common.people_to_org_relationships(id),
	
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);


-- FILES & DIRECTORIES ----------------------------------------------------------

create table common.directories (
  id serial primary key,

	parent int references common.directories(id),
  name varchar(255),
	-- todo

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.files (
  id serial primary key,

  directory int not null references common.directories(id),
	name varchar(255),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.file_versions (
  id serial primary key,

  category varchar(255),
  mime_type common.mime_type not null,
  name varchar(255) not null,
  file int not null references common.files(id),
  file_url varchar(255) not null,
  file_size int, -- bytes

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
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
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

ALTER TABLE common.tickets ADD CONSTRAINT common_tickets_parent_fk foreign key (parent) references common.tickets(id);

create table common.ticket_assignments (
	id serial primary key,

	ticket int not null references common.tickets(id),
	person int not null references admin.people(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.work_records(
	id serial primary key,

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
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.work_estimates(
	id serial primary key,

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
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
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
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

-- WORKFLOW -----------------------------------------------------------------

create table common.workflows(
	id serial primary key,

	title varchar(128) not null,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.stages(
	id serial primary key,

	title varchar(128) not null,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.stage_flows(
	id serial primary key,

	from_stage int not null references common.stages(id),
	to_stage int not null references common.stages(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.stage_grants(
	id serial primary key,

	stage int not null references common.stages(id),
	role int not null references admin.roles(id),
	group_id int not null references admin.groups(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
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
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);