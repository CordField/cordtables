-- system schema. org specific schema should go in an org-specific file.

-- ENUMS ----

-- todo
create type common.mime_type as enum (
  'A',
  'B',
  'C'
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

create table common.chats (
	id serial primary key,

	-- chats are different, talk to architect. note that some are nullable
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null references admin.people(id),
	peer int references admin.peers(id)

);

alter table admin.people add constraint admin_people_chat_fk foreign key (chat) references common.chats(id);
alter table admin.groups add constraint admin_group_row_access_chat_fk foreign key (chat) references common.chats(id);
alter table admin.group_row_access add constraint admin_group_row_access_chat_fk foreign key (chat) references common.chats(id);
alter table admin.group_memberships add constraint admin_group_memberships_chat_fk foreign key (chat) references common.chats(id);
alter table admin.roles add constraint admin_roles_chat_fk foreign key (chat) references common.chats(id);
alter table admin.role_column_grants add constraint admin_role_column_grants_chat_fk foreign key (chat) references common.chats(id);
alter table admin.role_table_permissions add constraint admin_role_table_permissions_chat_fk foreign key (chat) references common.chats(id);
alter table admin.role_memberships add constraint admin_role_memberships_chat_fk foreign key (chat) references common.chats(id);
alter table admin.users add constraint admin_users_chat_fk foreign key (chat) references common.chats(id);

create table common.posts (
	id serial primary key,

	chat int not null references common.chats(id),
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
	type location_type not null,

	
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
	primary_location int references locations(id),

  
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

	org int not null references organizations(id),
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

-- PROJECTS ------------------------------------------------------------------

create table common.projects (
	id serial primary key,
	group_id int not null references admin.groups(id),

	name varchar(32) not null,
	primary_org int references organizations(id),
	primary_location int references locations(id),
	sensitivity common.sensitivity default 'High',

	
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id),

	unique (primary_org, name)
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
	title varchar(125) not null,

  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.work_orders(
	id serial primary key,

	ticket int references common.tickets(id),
	content text not null,

  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.ticket_assignments (
	id serial primary key,

	ticket int not null references common.tickets(id),
	person_id int not null references admin.people(id),

  
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
	hours decimal not null,
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
	hours decimal not null,
	comment text,

  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

-- expected to be used only by admins
create table common.ticket_feedback_options (
  id serial primary key,
  option text,
  peer int references admin.peers(id)
);

create table common.ticket_feedback(
	id serial primary key,

	ticket int references common.tickets(id),
	stakeholder int not null references admin.people(id),
	feedback int not null references common.ticket_feedback_options,

  
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

	workflow int not null references common.workflows(id),
	title varchar(128) not null,

  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.work_order_templates(
	id serial primary key,

	stage int not null references common.stages(id),
	content text not null,

  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table common.stage_options(
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

create table common.stage_notifications(
	id serial primary key,

	stage int not null references common.stages(id),
	email varchar(64) not null,

  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);